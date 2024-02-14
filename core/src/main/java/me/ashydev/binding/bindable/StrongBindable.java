/*
 * Copyright (c) 2024 Ashley (uwuvern) <uwuvern@outlook.com>
 *
 * This project is licensed under the MIT license, check the root of the project for
 * more information.
 */

package me.ashydev.binding.bindable;

import me.ashydev.binding.IBindable;
import me.ashydev.binding.action.ValuedAction;
import me.ashydev.binding.action.event.ValueChangedEvent;
import me.ashydev.binding.action.queue.ValuedActionQueue;
import me.ashydev.binding.types.IHasDefault;

public class StrongBindable<T> extends Bindable<T> implements IHasDefault<T> {
    protected transient final ValuedActionQueue<T> defaultChanged = new ValuedActionQueue<>();
    protected T defaultValue;

    public StrongBindable() {
        super();
    }

    public StrongBindable(T defaultValue) {
        super(defaultValue);

        this.defaultValue = value;
    }

    public StrongBindable(T value, T defaultValue) {
        super(value);

        this.defaultValue = defaultValue;
    }

    @Override
    public StrongBindable<T> createInstance() {
        return new StrongBindable<>();
    }

    @Override
    public boolean isDefault() {
        return get().equals(getDefaultValue());
    }

    @Override
    public void setDefault() {
        set(defaultValue);
    }

    @Override
    public ValuedActionQueue<T> getDefaultChanged() {
        return defaultChanged;
    }

    @Override
    public T getDefaultValue() {
        return defaultValue;
    }

    @Override
    public void setDefaultValue(T defaultValue) {
        if (defaultValue == this.defaultValue) return;

        updateDefaultValue(defaultValue, false, null);
    }

    protected void updateDefaultValue(T value, boolean bypassChecks, StrongBindable<T> source) {
        T oldValue = this.defaultValue;
        this.defaultValue = value;

        triggerDefaultChanged(oldValue, value, bypassChecks, true, source);
    }

    protected void triggerDefaultChanged(
            T beforePropagation,
            T value,
            boolean bypassChecks,
            boolean propagateToBindings,
            StrongBindable<T> source
    ) {
        if (propagateToBindings || bypassChecks) {
            propagate(
                    (binding) -> binding instanceof StrongBindable<T>,
                    (binding) -> ((StrongBindable<T>) binding).updateDefaultValue(value, bypassChecks, source),
                    source
            );
        }

        if (beforePropagation != value)
            defaultChanged.execute(new ValueChangedEvent<>(beforePropagation, value));
    }

    @Override
    public void onDefaultChanged(ValuedAction<T> action, boolean runOnceImmediately) {
        defaultChanged.add(action);

        if (runOnceImmediately) {
            action.accept(new ValueChangedEvent<>(defaultValue, defaultValue));
        }
    }

    @Override
    public StrongBindable<T> copy() {
        StrongBindable<T> copy = createInstance();

        copy.bindTo(this);

        return copy;
    }

    @Override
    public Bindable<T> copyTo(IBindable<T> other) {
        super.copyTo(other);

        if (!(other instanceof StrongBindable<T> strongBindable))
            return (Bindable<T>) other;

        strongBindable.setDefaultValue(defaultValue);

        return strongBindable;
    }

    @Override
    public StrongBindable<T> getBoundCopy() {
        return (StrongBindable<T>) super.getBoundCopy();
    }

    @Override
    public StrongBindable<T> getUnboundCopy() {
        return (StrongBindable<T>) super.getUnboundCopy();
    }

    @Override
    public StrongBindable<T> getWeakCopy() {
        return (StrongBindable<T>) super.getWeakCopy();
    }

    @Override
    public StrongBindable<T> bindTo(IBindable<T> other) {
        return (StrongBindable<T>) super.bindTo(other);
    }

    @Override
    public StrongBindable<T> weakBind(IBindable<T> other) {
        return (StrongBindable<T>) super.weakBind(other);
    }
}
