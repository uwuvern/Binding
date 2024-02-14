/*
 * Copyright (c) 2024 Ashley (uwuvern) <uwuvern@outlook.com>
 *
 * This project is licensed under the MIT license, check the root of the project for
 * more information.
 */

package me.ashydev.binding.bindables.precision;

import me.ashydev.binding.action.Action;
import me.ashydev.binding.action.ValuedAction;
import me.ashydev.binding.action.event.ValueChangedEvent;
import me.ashydev.binding.action.queue.ValuedActionQueue;
import me.ashydev.binding.bindable.Bindable;
import me.ashydev.binding.bindable.StrongBindable;
import me.ashydev.binding.bindables.ranged.RangeConstrainedBindable;
import me.ashydev.binding.bindables.ranged.RangedBindable;

import java.lang.ref.WeakReference;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public abstract class PrecisionConstrainedBindable<V extends Number, T extends Number> extends StrongBindable<V> implements IPrecision<T> {
    private static final List<Class<?>> VALID_PRECISION_TYPES = List.of(Float.class, Double.class);
    private transient final ValuedActionQueue<T> precisionChanged = new ValuedActionQueue<>();
    private transient final ValuedActionQueue<T> defaultPrecisionChanged = new ValuedActionQueue<>();
    private T precision, defaultPrecision;
    public PrecisionConstrainedBindable(V value, T defaultPrecision) {
        super(value);
        this.precision = defaultPrecision;
        this.defaultPrecision = defaultPrecision;

        if (!(precision instanceof Comparable<?>))
            throw new IllegalArgumentException(String.format("%s is not comparable, and therefore cannot be used as a precision.", precision.getClass().getSimpleName()));

        if (isValidForPrecision(defaultPrecision))
            throw new IllegalArgumentException(String.format("Invalid type for precision %s. must be one of %s", precision.getClass().getSimpleName(), VALID_PRECISION_TYPES));
    }

    public PrecisionConstrainedBindable(T precision) {
        this(null, precision);
    }

    protected void propagatePrecision(Action<PrecisionConstrainedBindable<V, T>> propagation, PrecisionConstrainedBindable<V, T> source) {
        super.propagate(
                (binding) -> binding instanceof PrecisionConstrainedBindable<?,?>,
                (binding) -> propagation.accept((PrecisionConstrainedBindable<V, T>) binding),
                source
        );
    }

    private static <T extends Number> boolean isValidForPrecision(T precision) {
        return !VALID_PRECISION_TYPES.contains(precision.getClass());
    }

    @SuppressWarnings("unchecked")
    private static <V extends Number, T extends Number> V precisionFor(T precision, V truncated) {
        int precisionValue = (int) Math.log10(1.0 / precision.doubleValue());

        final BigDecimal value = BigDecimal.valueOf(truncated.doubleValue())
                .setScale(precisionValue, RoundingMode.HALF_UP);

        if (precision instanceof Float)
            return (V) (Number) value.floatValue();
        else if (precision instanceof Double)
            return (V) (Number) value.doubleValue();

        throw new IllegalArgumentException(String.format("Invalid type for precision %s. must be one of %s", precision.getClass().getSimpleName(), VALID_PRECISION_TYPES));
    }

    @Override
    public T getPrecision() {
        return precision;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void setPrecision(T precision) {
        if (precision.equals(this.precision)) return;
        if (!(precision instanceof Comparable<?>)) return;

        Comparable<Number> comparable = (Comparable<Number>) precision;

        if (comparable.compareTo(0) <= 0)
            throw new IllegalArgumentException(String.format("Precision must be greater than 0., but was %s.", precision));

        setPrecision(precision, true, this);
    }

    @Override
    public T getDefaultPrecision() {
        return defaultPrecision;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void setDefaultPrecision(T defaultPrecision) {
        if (defaultPrecision.equals(this.defaultPrecision)) return;
        if (!(defaultPrecision instanceof Comparable<?>)) return;

        Comparable<Number> comparable = (Comparable<Number>) defaultPrecision;

        if (comparable.compareTo(0) <= 0)
            throw new IllegalArgumentException(String.format("Default precision must be greater than 0., but was %s.", defaultPrecision));

        setDefaultPrecision(defaultPrecision, this);
    }

    @Override
    public void set(V value) {
        super.set(precisionFor(precision, value));
    }

    protected void setPrecision(T precision, boolean updateCurrentValue, PrecisionConstrainedBindable<V, T> source) {
        T previous = this.precision;
        this.precision = precision;

        triggerPrecisionChanged(previous, precision, updateCurrentValue, source);

        if (updateCurrentValue) {
            set(value);
        }
    }

    protected void triggerPrecisionChanged(
            T previous,
            T current,
            boolean updateCurrentValue,
            PrecisionConstrainedBindable<V, T> source
    ) {
        triggerPrecisionChanged(previous, current, false, updateCurrentValue, true, source);
    }

    protected void triggerPrecisionChanged(
            T previous,
            T current,
            boolean bypassChecks,
            boolean updateCurrentValue,
            boolean propagateToBindings,
            PrecisionConstrainedBindable<V, T> source
    ) {
        if (propagateToBindings || bypassChecks) {
            propagatePrecision((binding) -> binding.setPrecision(current, updateCurrentValue, source), source);
        }
    }

    protected void setDefaultPrecision(T defaultPrecision, PrecisionConstrainedBindable<V, T> source) {
        T previous = this.defaultPrecision;
        this.defaultPrecision = defaultPrecision;

        triggerDefaultPrecisionChanged(previous, defaultPrecision, source(source, this));
    }

    protected void triggerDefaultPrecisionChanged(
            T previous,
            T current,
            PrecisionConstrainedBindable<V, T> source
    ) {
        triggerDefaultPrecisionChanged(previous, current, false, true, source);
    }
    protected void triggerDefaultPrecisionChanged(T previous, T current, boolean bypassChecks, boolean propagateToBindings, PrecisionConstrainedBindable<V, T> source) {
        if (propagateToBindings || bypassChecks) {
            propagatePrecision((binding) -> binding.setDefaultPrecision(current, source), source);
        }

        if (!previous.equals(current) || bypassChecks) {
            defaultPrecisionChanged.execute(new ValueChangedEvent<>(previous, current));
        }
    }

    @Override
    public void unbindEvents() {
        super.unbindEvents();

        precisionChanged.clear();
    }

    @Override
    public void onPrecisionChanged(ValuedAction<T> action, boolean runOnceImmediately) {
        precisionChanged.add(action);

        if (runOnceImmediately) {
            action.accept(new ValueChangedEvent<>(precision, precision));
        }
    }

    @Override
    public void onDefaultPrecisionChanged(ValuedAction<T> action, boolean runOnceImmediately) {
        defaultPrecisionChanged.add(action);

        if (runOnceImmediately) {
            action.accept(new ValueChangedEvent<>(defaultPrecision, defaultPrecision));
        }
    }

    @Override
    public ValuedActionQueue<T> getPrecisionChanged() {
        return precisionChanged;
    }

    @Override
    public ValuedActionQueue<T> getDefaultPrecisionChanged() {
        return defaultPrecisionChanged;
    }
}
