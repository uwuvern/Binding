/*
 * Copyright (c) 2024 Ashley (uwuvern) <uwuvern@outlook.com>
 *
 * This project is licensed under the MIT license, check the root of the project for
 * more information.
 */

package me.ashydev.binding.bindable;

import me.ashydev.binding.IBindable;
import me.ashydev.binding.ILeasedBindable;
import me.ashydev.binding.IUnbindable;
import me.ashydev.binding.action.Action;
import me.ashydev.binding.action.ValuedAction;
import me.ashydev.binding.action.event.ValueChangedEvent;
import me.ashydev.binding.action.queue.ValuedActionQueue;
import me.ashydev.binding.common.reference.LockedWeakList;
import me.ashydev.binding.types.ILeaser;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;

public class Bindable<T> implements IBindable<T> {
    protected static <T, V extends Bindable<T>> V source(V source, V self) {
        return source != null ? source : self;
    }

    protected transient final WeakReference<Bindable<T>> weakReference = new WeakReference<>(this);

    protected transient final ValuedActionQueue<T> valueChanged = new ValuedActionQueue<>();
    protected transient final ValuedActionQueue<ILeaser.LeaseState> leaseChanged = new ValuedActionQueue<>();
    protected transient final ValuedActionQueue<Boolean> disabledChanged = new ValuedActionQueue<>();

    protected transient final LockedWeakList<Bindable<T>> bindings = new LockedWeakList<>();

    protected transient Class<T> type;
    protected transient LeasedBindable<T> leasedBindable;
    protected transient boolean disabled;

    protected T value;


    public Bindable() {
        this(null);
    }


    @SuppressWarnings("unchecked")
    public Bindable(T value) {
        if (value != null) {
            this.type = (Class<T>) value.getClass();
        }

        this.value = value;
        this.disabled = false;
    }

    protected void propagate(Action<Bindable<T>> propagation, Bindable<T> source) {
        Iterator<WeakReference<Bindable<T>>> iterator = bindings.iterator();

        while (iterator.hasNext()) {
            WeakReference<Bindable<T>> binding = iterator.next();

            if (binding.refersTo(source)) continue;

            Bindable<T> bindable = binding.get();

            if (bindable == null) {
                iterator.remove();

                continue;
            }

            propagation.accept(bindable);
        }
    }

    protected void propagate(Predicate<Bindable<T>> filter, Action<Bindable<T>> propagation, Bindable<T> source) {
        Iterator<WeakReference<Bindable<T>>> iterator = bindings.iterator();

        while (iterator.hasNext()) {
            WeakReference<Bindable<T>> binding = iterator.next();

            if (binding.refersTo(source)) continue;

            Bindable<T> bindable = binding.get();

            if (bindable == null) {
                iterator.remove();

                continue;
            }

            if (!filter.test(bindable)) continue;

            propagation.accept(bindable);
        }
    }

    @Override
    public T get() {
        return value;
    }

    @Override
    public void set(T value) {
        if (value == this.value) return;

        updateValue(value, null);
    }

    protected void updateValue(T value, Bindable<T> source) {
        T oldValue = this.value;
        this.value = value;

        triggerValueChanged(oldValue, value, source(source, this));
    }

    protected void triggerValueChanged(
            T beforePropagation,
            T value,
            Bindable<T> source
    ) {
       triggerValueChanged(
                beforePropagation,
                value,
                false,
                true,
                source
       );
    }

    protected void triggerValueChanged(
            T beforePropagation,
            T value,
            boolean bypassChecks,
            boolean propagateToBindings,
            Bindable<T> source
    ) {
        if (propagateToBindings || bypassChecks) propagate((bindable) -> bindable.set(value), source);

        if (beforePropagation != value || bypassChecks) {
            valueChanged.execute(new ValueChangedEvent<>(beforePropagation, value));
        }
    }

    protected void triggerChange() {
        triggerValueChanged(value, value, this);
        triggerDisabledChange(disabled, disabled, this);
    }

    @Override
    public void onValueChanged(ValuedAction<T> action, boolean runOnceImmediately) {
        valueChanged.add(action);

        if (runOnceImmediately) {
            action.accept(new ValueChangedEvent<>(value, value));
        }
    }

    @Override
    public ValuedActionQueue<T> getValueChanged() {
        return valueChanged;
    }

    protected void setDisabled(boolean value, Bindable<T> source) {
        boolean oldValue = this.disabled;
        disabled = value;

        triggerDisabledChange(oldValue, value, source(source, this));
    }

    protected void triggerDisabledChange(
            boolean beforePropagation,
            boolean value,
            Bindable<T> source
    ) {
        triggerDisabledChange(
                beforePropagation,
                value,
                false,
                true,
                source
        );
    }

    protected void triggerDisabledChange(
            boolean beforePropagation,
            boolean value,
            boolean bypassChecks,
            boolean propagateToBindings,
            Bindable<T> source
    ) {
        if (propagateToBindings || bypassChecks) propagate((bindable) -> bindable.setDisabled(value), source);

        if (beforePropagation != disabled || bypassChecks) {
            disabledChanged.execute(new ValueChangedEvent<>(beforePropagation, value));
        }
    }

    @Override
    public void onDisabledChanged(ValuedAction<Boolean> action, boolean runOnceImmediately) {
        disabledChanged.add(action);

        if (runOnceImmediately) {
            action.accept(new ValueChangedEvent<>(disabled, disabled));
        }
    }

    @Override
    public boolean isDisabled() {
        return disabled;
    }

    @Override
    public void setDisabled(boolean disabled) {
        if (disabled == this.disabled) return;

        setDisabled(disabled, null);
    }

    @Override
    public ValuedActionQueue<Boolean> getDisabledChanged() {
        return disabledChanged;
    }

    @Override
    public Bindable<T> createInstance() {
        return new Bindable<>();
    }

    @Override
    public Bindable<T> copy() {
        Bindable<T> copy = createInstance();

        copy.bindTo(this);

        return copy;
    }

    @Override
    public Bindable<T> copyTo(IBindable<T> other) {
        if (!(other instanceof Bindable)) return null;

        other.set(get());
        other.setDisabled(isDisabled());

        return (Bindable<T>) other;
    }

    @Override
    public Bindable<T> getBoundCopy() {
        return (Bindable<T>) IBindable.create(this);
    }

    @Override
    public Bindable<T> getUnboundCopy() {
        return copy();
    }

    @Override
    public Bindable<T> getWeakCopy() {
        return (Bindable<T>) IBindable.createWeak(this);
    }

    @Override
    public Bindable<T> bindTo(IBindable<T> other) {
        if (!(other instanceof Bindable<T> bindable)) return null;

        if (bindings.contains(bindable.weakReference))
            throw new IllegalArgumentException(String.format("Attempted to bind %s to %s, but it was already bound", this.getClass().getSimpleName(), other.getClass().getSimpleName()));

        bindable.copyTo(this);

        refer(bindable);

        bindable.refer(this);

        return this;
    }

    @Override
    public IBindable<T> weakBind(IBindable<T> other) {
        if (!(other instanceof Bindable<T> bindable)) return null;

        bindable.copyTo(this);

        bindable.refer(this);

        return this;
    }

    protected void refer(Bindable<T> bindable) {
        WeakReference<Bindable<T>> reference = bindable.weakReference;

        if (bindings.contains(reference))
            throw new IllegalArgumentException(String.format("Attempted to add a binding to %s from %s, but it was already bound", this.getClass().getSimpleName(), bindable.getClass().getSimpleName()));

        bindings.add(reference);
    }

    protected void unrefer(Bindable<T> bindable) {
        WeakReference<Bindable<T>> reference = bindable.weakReference;

        if (!bindings.contains(reference))
            throw new IllegalArgumentException(String.format("Attempted to remove a binding to %s from %s, but it was not bound", this.getClass().getSimpleName(), bindable.getClass().getSimpleName()));

        bindings.remove(reference);
    }

    @Override
    public void unbindEvents() {
        valueChanged.clear();
        disabledChanged.clear();
    }

    @Override
    public void unbindWeak() {
        for (WeakReference<Bindable<T>> binding : new ArrayList<>(bindings)) {
            Bindable<T> bindable = binding.get();

            if (bindable == null) {
                bindings.remove(binding);
                continue;
            }

            unbindWeakFrom(this);
        }

        bindings.clear();
    }

    @Override
    public void unbindBindings() {
        for (WeakReference<Bindable<T>> binding : new ArrayList<>(bindings)) {
            Bindable<T> bindable = binding.get();

            if (bindable == null) {
                bindings.remove(binding);
                continue;
            }

            bindable.unbindFrom(this);
        }

        bindings.clear();
    }

    @Override
    public void unbind() {
        unbindBindings();
        unbindWeak();
        unbindEvents();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void unbindFrom(IUnbindable other) {
        if (!(other instanceof Bindable)) return;
        Bindable<T> bindable = (Bindable<T>) other;

        unrefer(bindable);
        bindable.unrefer(this);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void unbindWeakFrom(IUnbindable other) {
        if (!(other instanceof Bindable)) return;
        Bindable<T> bindable = (Bindable<T>) other;

        bindable.unrefer(this);
    }

    private LeaseState getLeaseState() {
        return leasedBindable != null ? LeaseState.LEASED : LeaseState.NONE;
    }

    private void updateLeaseState(LeaseState value) {
        LeaseState oldValue = getLeaseState();
        leasedBindable = null;

        triggerLeaseChanged(oldValue, value);
    }

    protected void triggerLeaseChanged(LeaseState old, LeaseState value) {
        leaseChanged.execute(new ValueChangedEvent<>(old, value));
    }

    @Override
    public ValuedActionQueue<LeaseState> getLeaseChanged() {
        return leaseChanged;
    }

    @Override
    public LeasedBindable<T> begin(boolean revertValueOnReturn) {
        if (checkForLease(this))
            throw new IllegalStateException(String.format("Attempted to lease %s, but it was already leased.", this.getClass().getSimpleName()));

        leasedBindable = new LeasedBindable<>(this, revertValueOnReturn);

        updateLeaseState(LeaseState.LEASED);

        return leasedBindable;
    }

    @Override
    public LeasedBindable<T> begin() {
        return begin(true);
    }

    @Override
    public void end(ILeasedBindable<T> bindable) {
        if (getLeaseState() != LeaseState.LEASED)
            throw new IllegalStateException(String.format("Attempted to end a lease on %s, but it was not leased.", this.getClass().getSimpleName()));

        if (bindable != leasedBindable)
            throw new IllegalArgumentException(String.format("Attempted to end a lease on %s, but it was not the current lease.", this.getClass().getSimpleName()));

        leasedBindable = null;
        updateLeaseState(LeaseState.RETURNED);
    }

    @Override
    public void onLeaseChanged(ValuedAction<LeaseState> action, boolean runOnceImmediately) {
        leaseChanged.add(action);

        if (runOnceImmediately) {
            action.accept(new ValueChangedEvent<>(getLeaseState(), getLeaseState()));
        }
    }

    private boolean checkForLease(Bindable<T> source) {
        if (getLeaseState() == LeaseState.LEASED)
            return true;

        if (bindings.isEmpty())
            return false;

        AtomicBoolean found = new AtomicBoolean(false);

        propagate((bindable) -> found.set(found.get() | bindable.checkForLease(source)), source);

        return found.get();
    }

    @SuppressWarnings("unchecked")
    public Class<T> getType() {
        if (type == null && value != null)
            type = (Class<T>) value.getClass();

        return type;
    }
}
