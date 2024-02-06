/*
 * Copyright (c) 2024 Ashley (uwuvern) <uwuvern@outlook.com>
 *
 * This project is licensed under the MIT license, check the root of the project for
 * more information.
 */

package me.ashydev.binding.bindables.ranged;

import me.ashydev.binding.IBindable;
import me.ashydev.binding.action.ValuedAction;
import me.ashydev.binding.action.event.ValueChangedEvent;
import me.ashydev.binding.action.queue.ValuedActionQueue;
import me.ashydev.binding.bindable.Bindable;
import me.ashydev.binding.bindable.StrongBindable;

import java.lang.ref.WeakReference;
import java.util.ArrayList;


public abstract class RangeConstrainedBindable<T extends Number> extends StrongBindable<T> implements IMinMax<T> {
    private transient final ValuedActionQueue<T> minValueChanged = new ValuedActionQueue<>();
    private transient final ValuedActionQueue<T> maxValueChanged = new ValuedActionQueue<>();
    private transient final ValuedActionQueue<T> defaultMinValueChanged = new ValuedActionQueue<>();
    private transient final ValuedActionQueue<T> defaultMaxValueChanged = new ValuedActionQueue<>();
    private T min, max;
    private T defaultMin, defaultMax;
    public RangeConstrainedBindable(T value, T min, T max) {
        super(value);

        this.min = min;
        this.max = max;

        this.defaultMin = min;
        this.defaultMax = max;
    }

    public RangeConstrainedBindable(T min, T max) {
        this(min, min, max);
    }

    @SuppressWarnings("unchecked")
    public static <T extends Number> T convert(double input, Class<? extends T> type) {
        if (type == Integer.class) {
            return (T) Integer.valueOf((int) input);
        } else if (type == Long.class) {
            return (T) Long.valueOf((long) input);
        } else if (type == Float.class) {
            return (T) Float.valueOf((float) input);
        } else if (type == Double.class) {
            return (T) Double.valueOf(input);
        } else if (type == Short.class) {
            return (T) Short.valueOf((short) input);
        } else if (type == Byte.class) {
            return (T) Byte.valueOf((byte) input);
        } else {
            throw new IllegalArgumentException("Unsupported number type: " + type);
        }
    }

    @Override
    public void set(T value) {
        T clamped = clampValue(value);

        if (clamped.equals(this.value)) return;

        super.set(clamped);
    }

    @Override
    public T getMin() {
        return min;
    }

    @Override
    public void setMin(T min) {
        if (min.equals(this.min)) return;

        setMinValue(min, true, this);
    }

    protected void setMinValue(T minValue, boolean updateCurrentValue, RangeConstrainedBindable<T> source) {
        T previous = min;

        this.min = minValue;
        triggerMinValueChange(previous, source, true);

        if (updateCurrentValue)
            set(value);
    }

    @Override
    protected void triggerChange() {
        super.triggerChange();

        triggerMinValueChange(this, false);
        triggerMaxValueChange(this, false);
    }

    protected void triggerMinValueChange(T beforePropagation, RangeConstrainedBindable<T> source, boolean propagateToBindings) {
        if (propagateToBindings)
            propagateMinValueChange(source);

        if (!beforePropagation.equals(min))
            minValueChanged.execute(new ValueChangedEvent<>(beforePropagation, min));
    }

    protected void triggerMinValueChange(RangeConstrainedBindable<T> source, boolean propagateToBindings) {
        triggerMinValueChange(min, source, propagateToBindings);
    }

    protected void propagateMinValueChange(RangeConstrainedBindable<T> source) {
        for (WeakReference<Bindable<T>> binding : new ArrayList<>(bindings)) {
            if (binding.refersTo(source)) continue;

            Bindable<T> bindable = binding.get();

            if (bindable == null) {
                bindings.remove(binding);
                continue;
            }

            if (bindable instanceof RangeConstrainedBindable<T> rangeConstrainedBindable) {
                rangeConstrainedBindable.setMinValue(min, true, this);
            }
        }
    }

    @Override
    public T getMax() {
        return max;
    }

    @Override
    public void setMax(T max) {
        if (max.equals(this.max)) return;

        setMaxValue(max, true, this);
    }

    protected void setMaxValue(T maxValue, boolean updateCurrentValue, RangeConstrainedBindable<T> source) {
        T previous = max;

        this.max = maxValue;
        triggerMaxValueChange(previous, source, true);

        if (updateCurrentValue)
            set(value);
    }

    protected void triggerMaxValueChange(T beforePropagation, RangeConstrainedBindable<T> source, boolean propagateToBindings) {
        if (propagateToBindings)
            propagateMaxValueChange(source);

        if (!beforePropagation.equals(max))
            maxValueChanged.execute(new ValueChangedEvent<>(beforePropagation, max));
    }

    protected void triggerMaxValueChange(RangeConstrainedBindable<T> source, boolean propagateToBindings) {
        triggerMaxValueChange(max, source, propagateToBindings);
    }

    protected void propagateMaxValueChange(RangeConstrainedBindable<T> source) {
        for (WeakReference<Bindable<T>> binding : new ArrayList<>(bindings)) {
            if (binding.refersTo(source)) continue;

            Bindable<T> bindable = binding.get();

            if (bindable == null) {
                bindings.remove(binding);
                continue;
            }

            if (bindable instanceof RangeConstrainedBindable<T> rangeConstrainedBindable) {
                rangeConstrainedBindable.setMaxValue(max, true, this);
            }
        }
    }

    @Override
    public Bindable<T> copyTo(IBindable<T> other) {
        if (other instanceof RangeConstrainedBindable<T> rangeConstrainedBindable) {
            rangeConstrainedBindable.setMin(min);
            rangeConstrainedBindable.setMax(max);

            rangeConstrainedBindable.setDefaultMax(defaultMax);
            rangeConstrainedBindable.setDefaultMin(defaultMin);
        }

        return super.copyTo(other);
    }

    @Override
    public RangeConstrainedBindable<T> bindTo(IBindable<T> other) {
        if (other instanceof RangeConstrainedBindable<T> rangeConstrainedBindable) {
            if (!isValidRange(rangeConstrainedBindable.min, rangeConstrainedBindable.max)) {
                throw new IllegalArgumentException(String.format("The target bindable %s has specified an invalid range of %s to %s", other, rangeConstrainedBindable.min, rangeConstrainedBindable.max));
            }
        }

        return (RangeConstrainedBindable<T>) super.bindTo(other);
    }

    @Override
    public T getDefaultMin() {
        return defaultMin;
    }

    @Override
    public void setDefaultMin(T min) {
        if (min.equals(this.min)) return;

        triggerDefaultMinValueChange(this, true, min);
    }

    @Override
    public T getDefaultMax() {
        return defaultMax;
    }

    @Override
    public void setDefaultMax(T max) {
        if (max.equals(this.max)) return;

        triggerDefaultMaxValueChange(this, true, max);
    }

    protected void triggerDefaultMinValueChange(RangeConstrainedBindable<T> source, boolean propagateToBindings, T min) {
        T previous = defaultMin;
        this.defaultMin = min;

        if (propagateToBindings)
            propagateDefaultMinValueChange(source);

        defaultMinValueChanged.execute(new ValueChangedEvent<>(previous, min));
    }

    protected void propagateDefaultMinValueChange(RangeConstrainedBindable<T> source) {
        for (WeakReference<Bindable<T>> binding : new ArrayList<>(bindings)) {
            if (binding.refersTo(source)) continue;

            Bindable<T> bindable = binding.get();

            if (bindable == null) {
                bindings.remove(binding);
                continue;
            }

            if (bindable instanceof RangeConstrainedBindable<T> rangeConstrainedBindable) {
                rangeConstrainedBindable.setDefaultMin(defaultMin);
            }
        }
    }

    protected void triggerDefaultMaxValueChange(RangeConstrainedBindable<T> source, boolean propagateToBindings, T max) {
        T previous = defaultMax;
        this.defaultMax = max;

        if (propagateToBindings)
            propagateDefaultMaxValueChange(source);

        defaultMaxValueChanged.execute(new ValueChangedEvent<>(previous, max));
    }

    protected void propagateDefaultMaxValueChange(RangeConstrainedBindable<T> source) {
        for (WeakReference<Bindable<T>> binding : new ArrayList<>(bindings)) {
            if (binding.refersTo(source)) continue;

            Bindable<T> bindable = binding.get();

            if (bindable == null) {
                bindings.remove(binding);
                continue;
            }

            if (bindable instanceof RangeConstrainedBindable<T> rangeConstrainedBindable) {
                rangeConstrainedBindable.setDefaultMax(defaultMax);
            }
        }
    }

    @Override
    public void setDefault() {
        super.setDefault();

        setMin(defaultMin);
        setMax(defaultMax);
    }

    @Override
    public float normalized() {
        float min = this.min.floatValue();
        float max = this.max.floatValue();

        if (min - max <= 0)
            return 1;

        float value = this.value.floatValue();
        return (value - min) / (max - min);
    }

    @Override
    public boolean hasDefinedRange() {
        return !min.equals(defaultMin) ||
                !max.equals(defaultMax);
    }

    @Override
    public void onMinChanged(ValuedAction<T> action, boolean runOnceImmediately) {
        minValueChanged.add(action);

        if (runOnceImmediately)
            action.invoke(new ValueChangedEvent<>(min, min));
    }

    @Override
    public void onMaxChanged(ValuedAction<T> action, boolean runOnceImmediately) {
        maxValueChanged.add(action);

        if (runOnceImmediately)
            action.invoke(new ValueChangedEvent<>(max, max));
    }

    @Override
    public void onDefaultMinChanged(ValuedAction<T> action, boolean runOnceImmediately) {
        defaultMinValueChanged.add(action);

        if (runOnceImmediately)
            action.invoke(new ValueChangedEvent<>(defaultMin, defaultMin));
    }

    @Override
    public void onDefaultMaxChanged(ValuedAction<T> action, boolean runOnceImmediately) {
        defaultMaxValueChanged.add(action);

        if (runOnceImmediately)
            action.invoke(new ValueChangedEvent<>(defaultMax, defaultMax));
    }

    @Override
    public void unbindEvents() {
        super.unbindEvents();

        minValueChanged.clear();
        maxValueChanged.clear();

        defaultMinValueChanged.clear();
        defaultMaxValueChanged.clear();
    }

    @Override
    public RangeConstrainedBindable<T> getBoundCopy() {
        return (RangeConstrainedBindable<T>) super.getBoundCopy();
    }

    @Override
    public RangeConstrainedBindable<T> getUnboundCopy() {
        return (RangeConstrainedBindable<T>) super.getUnboundCopy();
    }

    @Override
    public RangeConstrainedBindable<T> getWeakCopy() {
        return (RangeConstrainedBindable<T>) super.getWeakCopy();
    }

    protected T clampValue(T value) {
        if (min.doubleValue() > value.doubleValue())
            return min;
        else if (max.doubleValue() < value.doubleValue())
            return max;

        return value;
    }

    protected boolean isValidRange(T min, T max) {
        return min.doubleValue() <= max.doubleValue();
    }

    protected boolean isValueInRange(T value) {
        return value.doubleValue() >= min.doubleValue() && value.doubleValue() <= max.doubleValue();
    }

    @Override
    public ValuedActionQueue<T> getOnMinChanged() {
        return minValueChanged;
    }

    @Override
    public ValuedActionQueue<T> getOnMaxChanged() {
        return maxValueChanged;
    }

    @Override
    public ValuedActionQueue<T> getOnDefaultMinChanged() {
        return defaultMinValueChanged;
    }

    @Override
    public ValuedActionQueue<T> getOnDefaultMaxChanged() {
        return defaultMaxValueChanged;
    }
}
