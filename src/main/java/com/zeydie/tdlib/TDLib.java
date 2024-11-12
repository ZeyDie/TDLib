package com.zeydie.tdlib;

import com.google.common.util.concurrent.Service;
import com.zeydie.sgson.SGsonFile;
import com.zeydie.tdlib.configs.AuthConfig;
import com.zeydie.tdlib.configs.TDLibConfig;
import com.zeydie.tdlib.handlers.auth.UpdateAuthorizationResultHandler;
import com.zeydie.tdlib.handlers.basis.UpdateResultHandler;
import com.zeydie.tdlib.handlers.chat.UpdateChatResultHandler;
import com.zeydie.tdlib.schedulers.LoadChatsScheduler;
import lombok.*;
import lombok.extern.log4j.Log4j2;
import org.drinkless.tdlib.Client;
import org.drinkless.tdlib.TdApi;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

@Log4j2
public final class TDLib {
    @Getter
    private static @NotNull TDLib instance = new TDLib();

    public static void main(@Nullable final String[] args) {
        instance.preInit();
    }

    public static Client getClient() {
        return instance.client;
    }

    private Client client;
    @Setter
    @Getter
    private boolean started;
    @Setter
    @Getter
    private int verbosityLevelConsole = 2;
    @Setter
    @Getter
    private int verbosityLevelFile = 3;

    private static @NotNull Path TDLIB = Paths.get("tdlib");
    private static @NotNull Path WINDOWS_TDLIB = TDLIB.resolve("windows");
    private static @NotNull Path LINUX_TDLIB = TDLIB.resolve("linux");

    public static @NotNull Path WINDOWS_LIBCRYPTO_X64 = WINDOWS_TDLIB.resolve("libcrypto-1_1-x64.dll");
    public static @NotNull Path WINDOWS_LIBSSL_X64 = WINDOWS_TDLIB.resolve("libssl-1_1-x64.dll");
    public static @NotNull Path WINDOWS_ZLIB = WINDOWS_TDLIB.resolve("zlib1.dll");
    public static @NotNull Path WINDOWS_TDJNI = WINDOWS_TDLIB.resolve("tdjni.dll");

    public static @NotNull Path LINUX_TDJNI = LINUX_TDLIB.resolve("tdjni.so");

    @Getter
    private static @NotNull AuthConfig authConfig = SGsonFile.createPretty(TDLIB.resolve("auth.jcfg")).fromJsonToObject(new AuthConfig());
    @Getter
    private static @NotNull TDLibConfig tdLibConfig = SGsonFile.createPretty(TDLIB.resolve("tdlib.jcfg")).fromJsonToObject(new TDLibConfig());

    @Getter
    private static final @NotNull UpdateAuthorizationResultHandler authorizationResultHandler = new UpdateAuthorizationResultHandler();
    @Getter
    private static final @NotNull UpdateChatResultHandler chatResultHandler = new UpdateChatResultHandler();

    private @NotNull Service loadChatsScheduler;

    private void preInit() {
        log.debug("================PREINIT==================");
        log.debug("AuthConfig: {}", this.authConfig);
        log.debug("TdLibConfig: {}", this.tdLibConfig);

        @NonNull val os = this.getOs();
        @NonNull val arch = this.getArch();

        log.debug("{} - {}", os, arch);

        if (os != null)
            if (os.toLowerCase(Locale.ROOT).startsWith("windows")) {
                extractAndLoadDll(WINDOWS_LIBCRYPTO_X64);
                extractAndLoadDll(WINDOWS_LIBSSL_X64);
                extractAndLoadDll(WINDOWS_ZLIB);
                extractAndLoadDll(WINDOWS_TDJNI);
            } else if (os.toLowerCase(Locale.ROOT).startsWith("linux"))
                extractAndLoadDll(LINUX_TDJNI);

        this.init();
    }

    public @NotNull String getOs() {
        return System.getProperty("os.name");
    }

    public @NotNull String getArch() {
        return System.getProperty("os.arch");
    }

    public void extractAndLoadDll(@NonNull final Path path) {
        this.extractDll(path);
        this.loadDll(path);
    }

    @SneakyThrows
    public void extractDll(@NonNull final Path path) {
        @NonNull var name = path.toString();

        while (name.contains("\\"))
            name = name.replace("\\", "/");

        if (this.getOs().toLowerCase(Locale.ROOT).startsWith("linux"))
            name = Paths.get(name).getParent().resolve("lib" + path.getFileName()).toString();

        @Cleanup val inputStream = TDLib.class.getClassLoader().getResourceAsStream(name);

        if (inputStream != null) {
            val libPath = Paths.get(System.getProperties().getProperty("java.home"))
                    .resolve("bin")
                    .resolve(Paths.get(name).getFileName());
            Files.copy(
                    inputStream,
                    libPath,
                    StandardCopyOption.REPLACE_EXISTING
            );

            log.debug("Extracted {}", libPath);
        } else log.warn("No found {} ({})!", name, inputStream);
    }

    public void loadDll(@NonNull final Path path) {
        System.loadLibrary(
                path.toFile().getName()
                        .replaceAll("\\.so", "")
                        .replaceAll("\\.dll", "")
        );
    }

    @SneakyThrows
    private void init() {
        log.debug("================INIT==================");

        Client.setLogMessageHandler(this.verbosityLevelConsole, (verbosityLevel, message) -> log.debug(message));

        Client.execute(new TdApi.SetLogVerbosityLevel(this.verbosityLevelFile));
        Client.execute(new TdApi.SetLogStream(new TdApi.LogStreamFile("logs/tdlib.log", Integer.MAX_VALUE, false)));

        this.client = Client.create(new UpdateResultHandler(), exception -> log.error(exception.getMessage(), exception), exception -> log.error(exception.getMessage(), exception));

        while (!this.isStarted()) ;
    }

    public void startSchedulers() {
        this.loadChatsScheduler = new LoadChatsScheduler();
    }

    public void stop() {
        this.client.send(new TdApi.Close(), log::debug);
    }

    @SneakyThrows
    public static @Nullable String readConsole() {
        val inputStream = new InputStreamReader(System.in);
        val bufferedReader = new BufferedReader(inputStream);

        val line = bufferedReader.readLine();

        inputStream.close();
        bufferedReader.close();

        return line;
    }

    public <T extends TdApi.Object> T getAfterFinished(
            @NonNull final AtomicReference<T> atomicReference,
            @NonNull final AtomicBoolean atomicBoolean
    ) {
        while (!atomicBoolean.get()) ;

        return atomicReference.get();
    }

    public @Nullable TdApi.Chat getChat(final long id) {
        @NonNull val atomicValue = new AtomicReference<TdApi.Chat>();
        @NonNull val atomicFinished = new AtomicBoolean(false);

        this.client.send(
                new TdApi.GetChat(id),
                object -> {
                    if (object instanceof @NonNull final TdApi.Chat chat) {
                        log.debug("GetChat {} {}", chat.id, chat.title);

                        atomicValue.set(chat);
                    } else log.error(object);

                    atomicFinished.set(true);
                }
        );

        return this.getAfterFinished(atomicValue, atomicFinished);
    }
}