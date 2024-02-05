import me.ashydev.bindable.bindable.Bindable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

public class IntegerBindingTest {
    private Bindable<Integer> bindable, other;

    @BeforeEach
    public void setUp() {
        bindable = new Bindable<>(0);
        other = new Bindable<>(0);
    }

    @Test
    public void testBind() {
        other.bindTo(bindable);
        bindable.set(400);

        assert other.get() == 400;
    }

    @Test
    public void testWeakBind() {
        other.weakBind(bindable);
        bindable.set(400);

        assert other.get() == 400;
    }

    @Test
    public void testUnbind() {
        other.bindTo(bindable);
        bindable.set(400);
        other.unbind();
        bindable.set(200);

        assert other.get() == 400;
    }

    @Test
    public void testWeakImmutability() {
        other.weakBind(bindable);
        bindable.set(400);
        other.set(200);

        assert bindable.get() == 400;
    }

    @Test
    public void testBindMutability() {
        other.bindTo(bindable);
        bindable.set(400);
        other.set(200);

        assert bindable.get() == 200;
    }
}
