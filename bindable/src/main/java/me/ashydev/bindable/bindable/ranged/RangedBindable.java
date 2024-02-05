package me.ashydev.bindable.bindable.ranged;

public class RangedBindable<T extends Number> extends RangeConstrainedBindable<T> {
    public RangedBindable(T value, T min, T max) {
        super(value, min, max);
    }

    public RangedBindable(T min, T max) {
        super(min, max);
    }
}
