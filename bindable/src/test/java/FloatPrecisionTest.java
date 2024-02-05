import me.ashydev.bindable.bindable.precision.PrecisionBindable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class FloatPrecisionTest {
    private PrecisionBindable<Float> bindable;

    @BeforeEach
    public void setUp() {
        bindable = new PrecisionBindable<>(0.0f, 0.01f);
    }

    @Test
    public void testSetHalfUp() {
        bindable.set(0.0183672f);

        assert bindable.get() == 0.02f;
    }

    @Test
    public void testSetHalfDown() {
        bindable.set(0.0133672f);

        assert bindable.get() == 0.01f;
    }
}
