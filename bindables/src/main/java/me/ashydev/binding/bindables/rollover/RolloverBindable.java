package me.ashydev.binding.bindables.rollover;

import me.ashydev.binding.bindable.Bindable;
import me.ashydev.binding.bindables.ranged.RangeConstrainedBindable;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class RolloverBindable<T> extends Bindable<T> implements Rollable<T> {
    private int index;
    private final List<T> values;

    @SafeVarargs
    public RolloverBindable(T... values) {
        this(0, values);
    }

    @SafeVarargs
    public RolloverBindable(int index, T... values) {
        super(values[index]);

        this.index = index;
        this.values = Arrays.asList(values);
    }


    @Override
    public void set(T value) {
        if (!values.contains(value)) return;

        set(values.indexOf(value));
    }

    @Override
    public void set(int index) {
        this.index = recalculate(index);

        update();
    }

    protected void update() {
        super.set(values.get(this.index));
    }

    @Override
    public T get(int index) {
        return values.get(recalculate(index));
    }

    @Override
    public int min() {
        return 0;
    }

    @Override
    public int max() {
        return values.size();
    }

    @Override
    public int index() {
        return index;
    }
}
