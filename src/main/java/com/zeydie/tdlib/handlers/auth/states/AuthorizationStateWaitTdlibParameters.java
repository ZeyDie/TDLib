package com.zeydie.tdlib.handlers.auth.states;

import com.zeydie.tdlib.TDLib;
import com.zeydie.tdlib.handlers.basis.IStateHandler;
import com.zeydie.tdlib.handlers.connection.ConnectionStateReadyResultHandler;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import org.drinkless.tdlib.TdApi;
import org.jetbrains.annotations.NotNull;

@Log4j2
@RequiredArgsConstructor
public final class AuthorizationStateWaitTdlibParameters implements IStateHandler {
    private final int apiId;
    private final @NotNull String apiHash;
    private final boolean useTestServer;

    @Getter
    private final int constructor = TdApi.AuthorizationStateWaitTdlibParameters.CONSTRUCTOR;

    @Override
    public void onResult(@NonNull final TdApi.Object object) {
        log.debug("Result: {}", object);

        @NonNull val parameters = new TdApi.SetTdlibParameters();

        parameters.apiId = this.apiId;
        parameters.apiHash = this.apiHash;
        parameters.useTestDc = this.useTestServer;

        parameters.databaseDirectory = "tdlib_database";
        parameters.useMessageDatabase = true;
        parameters.useSecretChats = true;
        parameters.systemLanguageCode = "en";
        parameters.deviceModel = "Desktop";
        parameters.applicationVersion = "1.0";
        parameters.enableStorageOptimizer = true;

        TDLib.getClient().send(parameters, new ConnectionStateReadyResultHandler());
    }
}