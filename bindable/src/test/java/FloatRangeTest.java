import me.ashydev.bindable.bindable.ranged.RangedBindable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class FloatRangeTest {
    private RangedBindable<Float> rangedBindable;

    @BeforeEach
    public void setUp() {
        rangedBindable = new RangedBindable<>(0.0f, 0.0f, 100.0f);
    }

    @Test
    public void testSet() {
        rangedBindable.set(50.0f);

        assert rangedBindable.get() == 50.0f;
    }

    @Test
    public void testSetBelow() {
        rangedBindable.set(-50.0f);

        assert rangedBindable.get() == 0.0f;
    }

    @Test
    public void testSetAbove() {
        rangedBindable.set(150.0f);

        assert rangedBindable.get() == 100.0f;
    }
}
