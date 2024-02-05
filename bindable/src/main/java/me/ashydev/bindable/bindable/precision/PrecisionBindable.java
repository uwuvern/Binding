package me.ashydev.bindable.bindable.precision;

public class PrecisionBindable<T extends Number> extends PrecisionConstrainedBindable<T, Float> {
    public PrecisionBindable(T value, float precision) {
        super(value, precision);
    }

    public PrecisionBindable(float precision) {
        super(precision);
    }
}
