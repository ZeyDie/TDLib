import com.zeydie.tdlib.TDLib;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static com.zeydie.tdlib.TDLib.*;

public final class ExtractDllTest {
    private final @NotNull TDLib tdLib = TDLib.getInstance();
    private final @NotNull String os = this.tdLib.getOs().toLowerCase(Locale.ROOT);

    @Test
    public void linuxTDJNI() {
        if (this.os.startsWith("linux")) {
            this.tdLib.extractDll(LINUX_TDJNI);

            Assertions.assertTrue(LINUX_TDJNI.toFile().exists());
        }
    }

    @Test
    public void windowsTDJNI() {
        if (this.os.startsWith("windows")) {
            this.tdLib.extractDll(WINDOWS_TDJNI);

            Assertions.assertTrue(WINDOWS_TDJNI.toFile().exists());
        }
    }

    @Test
    public void windowsCryptoX64() {
        if (this.os.startsWith("windows")) {
            this.tdLib.extractDll(WINDOWS_LIBCRYPTO_X64);

            Assertions.assertTrue(WINDOWS_LIBCRYPTO_X64.toFile().exists());
        }
    }

    @Test
    public void windowsSSLX64() {
        if (this.os.startsWith("windows")) {
            this.tdLib.extractDll(WINDOWS_LIBSSL_X64);

            Assertions.assertTrue(WINDOWS_LIBSSL_X64.toFile().exists());
        }
    }

    @Test
    public void windowsZLib() {
        if (this.os.startsWith("windows")) {
            this.tdLib.extractDll(WINDOWS_ZLIB);

            Assertions.assertTrue(WINDOWS_ZLIB.toFile().exists());
        }
    }
}