import com.zeydie.tdlib.TDLib;
import lombok.extern.log4j.Log4j;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.ClassOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestClassOrder;

@Log4j
@TestClassOrder(ClassOrderer.OrderAnnotation.class)
public final class AuthTest {
    private final @NotNull TDLib tdLib = TDLib.getInstance();

    @Test
    @Order(1)
    public void loadTest() {
        this.tdLib.preInit();
        this.tdLib.init();
    }
}