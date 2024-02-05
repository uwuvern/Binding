/*
 * Copyright (c) 2024 Ashley (uwuvern) <uwuvern@outlook.com>
 *
 * This project is licensed under the MIT license, check the root of the project for
 * more information.
 */

package me.ashydev.bindable.bindable.ranged;

import me.ashydev.bindable.IBindable;
import me.ashydev.bindable.action.ValuedAction;
import me.ashydev.bindable.action.queue.ActionQueue;
import me.ashydev.bindable.bindable.Bindable;
import me.ashydev.bindable.event.ValueChangedEvent;

import java.lang.ref.WeakReference;

public abstract class RangeConstrainedBindable<T extends Number> extends Bindable<T> implements IMinMax<T> {
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

    private final ActionQueue<ValueChangedEvent<T>> minValueChanged = new ActionQueue<>();
    private final ActionQueue<ValueChangedEvent<T>> maxValueChanged = new ActionQueue<>();

    private final ActionQueue<ValueChangedEvent<T>> defaultMinValueChanged = new ActionQueue<>();
    private final ActionQueue<ValueChangedEvent<T>> defaultMaxValueChanged = new ActionQueue<>();

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

    @Override
    public void set(T value) {
        if (value.equals(this.value)) return;

        super.set(clampValue(value));
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
        for (WeakReference<Bindable<T>> binding : bindings) {
            if (binding.refersTo(source)) continue;

            Bindable<T> bindable = binding.get();

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
        for (WeakReference<Bindable<T>> binding : bindings) {
            if (binding.refersTo(source)) continue;

            Bindable<T> bindable = binding.get();

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
    public T getDefaultMax() {
        return defaultMax;
    }

    @Override
    public void setDefaultMin(T min) {
        if (min.equals(this.min)) return;

        triggerDefaultMinValueChange(this, true, min);
    }

    protected void triggerDefaultMinValueChange(RangeConstrainedBindable<T> source, boolean propagateToBindings, T min) {
        T previous = defaultMin;
        this.defaultMin = min;

        if (propagateToBindings)
            propagateDefaultMinValueChange(source);

        defaultMinValueChanged.execute(new ValueChangedEvent<>(previous, min));
    }

    protected void propagateDefaultMinValueChange(RangeConstrainedBindable<T> source) {
        for (WeakReference<Bindable<T>> binding : bindings) {
            if (binding.refersTo(source)) continue;

            Bindable<T> bindable = binding.get();

            if (bindable instanceof RangeConstrainedBindable<T> rangeConstrainedBindable) {
                rangeConstrainedBindable.setDefaultMin(defaultMin);
            }
        }
    }

    @Override
    public void setDefaultMax(T max) {
        if (max.equals(this.max)) return;

        triggerDefaultMaxValueChange(this, true, max);
    }

    protected void triggerDefaultMaxValueChange(RangeConstrainedBindable<T> source, boolean propagateToBindings, T max) {
        T previous = defaultMax;
        this.defaultMax = max;

        if (propagateToBindings)
            propagateDefaultMaxValueChange(source);

        defaultMaxValueChanged.execute(new ValueChangedEvent<>(previous, max));
    }

    protected void propagateDefaultMaxValueChange(RangeConstrainedBindable<T> source) {
        for (WeakReference<Bindable<T>> binding : bindings) {
            if (binding.refersTo(source)) continue;

            Bindable<T> bindable = binding.get();

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

    public void onMinValueChanged(ValuedAction<T> action, boolean runOnceImmediately) {
        minValueChanged.add(action);

        if (runOnceImmediately)
            action.invoke(new ValueChangedEvent<>(min, min));
    }

    public void onMinValueChanged(ValuedAction<T> action) {
        onMinValueChanged(action, false);
    }

    public void onMaxValueChanged(ValuedAction<T> action, boolean runOnceImmediately) {
        maxValueChanged.add(action);

        if (runOnceImmediately)
            action.invoke(new ValueChangedEvent<>(max, max));
    }

    public void onMaxValueChanged(ValuedAction<T> action) {
        onMaxValueChanged(action, false);
    }

    public void onDefaultMinValueChanged(ValuedAction<T> action, boolean runOnceImmediately) {
        defaultMinValueChanged.add(action);

        if (runOnceImmediately)
            action.invoke(new ValueChangedEvent<>(defaultMin, defaultMin));
    }

    public void onDefaultMinValueChanged(ValuedAction<T> action) {
        onDefaultMinValueChanged(action, false);
    }

    public void onDefaultMaxValueChanged(ValuedAction<T> action, boolean runOnceImmediately) {
        defaultMaxValueChanged.add(action);

        if (runOnceImmediately)
            action.invoke(new ValueChangedEvent<>(defaultMax, defaultMax));
    }

    public void onDefaultMaxValueChanged(ValuedAction<T> action) {
        onDefaultMaxValueChanged(action, false);
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
        double min = getMin().doubleValue();
        double max = getMax().doubleValue();

        return convert(Math.min(Math.max(value.doubleValue(), min), max), type);
    }

    protected boolean isValidRange(T min, T max) {
        return min.doubleValue() <= max.doubleValue();
    }

    protected boolean isValueInRange(T value) {
        return value.doubleValue() >= min.doubleValue() && value.doubleValue() <= max.doubleValue();
    }
}
