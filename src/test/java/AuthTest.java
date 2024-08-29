import com.zeydie.tdlib.TDLib;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.*;

@TestClassOrder(ClassOrderer.OrderAnnotation.class)
public final class AuthTest {
    private final @NotNull TDLib tdLib = TDLib.getInstance();

    @Test
    @Order(1)
    public void loadTest() {
        this.tdLib.preInit();
        this.tdLib.init();
        this.tdLib.postInit();

        Assertions.assertTrue(this.tdLib.isStarted());
    }

    @Test
    @Order(2)
    public void getChatTest() {

    }
}