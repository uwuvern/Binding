/*
 * Copyright (c) 2024 Ashley (uwuvern) <uwuvern@outlook.com>
 *
 * This project is licensed under the MIT license, check the root of the project for
 * more information.
 */

package me.ashydev.bindable.bindable;

import me.ashydev.bindable.IBindable;
import me.ashydev.bindable.ILeasedBindable;
import me.ashydev.bindable.IUnbindable;
import me.ashydev.bindable.action.Action;
import me.ashydev.bindable.action.ValuedAction;
import me.ashydev.bindable.action.queue.ActionQueue;
import me.ashydev.bindable.event.ValueChangedEvent;
import me.ashydev.bindable.reference.LockedWeakList;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class Bindable<T> implements IBindable<T> {
    protected transient final WeakReference<Bindable<T>> weakReference = new WeakReference<>(this);

    protected transient final ActionQueue<ValueChangedEvent<T>> valueChanged = new ActionQueue<>();
    protected transient final ActionQueue<ValueChangedEvent<LeaseState>> leaseChanged = new ActionQueue<>();
    protected transient final ActionQueue<ValueChangedEvent<Boolean>> disabledChanged = new ActionQueue<>();

    public transient final LockedWeakList<Bindable<T>> bindings = new LockedWeakList<>();

    protected transient Class<T> type;

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


    @Override
    public T get() {
        return value;
    }

    @Override
    public void set(T value) {
        if (value == this.value) return;

        updateValue(value, false, null);
    }

    protected void updateValue(T value, boolean bypassChecks, Bindable<T> source) {
        T oldValue = this.value;
        this.value = value;

        triggerValueChanged(oldValue, source != null ? source : this, value, true, bypassChecks);
    }

    protected void triggerValueChanged(T beforePropagation, Bindable<T> source, T value, boolean propagate, boolean bypassChecks) {
        if (propagate)
            propagateValueChanged(source, value);

        if (beforePropagation != value)
            valueChanged.execute(new ValueChangedEvent<>(beforePropagation, value));
    }

    protected void triggerChange() {
        triggerValueChanged(value, this, value, false, false);
        triggerDisabledChange(disabled, this, false, false);
    }

    protected void propagateValueChanged(Bindable<T> source, T value) {
        gc();

        for (WeakReference<Bindable<T>> binding : bindings) {
            if (binding.refersTo(source)) continue;

            Bindable<T> bindable = binding.get();

            if (bindable == null) continue;

            bindable.set(value);
        }
    }

    @Override
    public void onValueChanged(ValuedAction<T> action, boolean runOnceImmediately) {
        valueChanged.add(action);

        if (runOnceImmediately)
            action.invoke(new ValueChangedEvent<>(value, value));
    }

    @Override
    public ActionQueue<ValueChangedEvent<T>> getValueChanged() {
        return valueChanged;
    }

    @Override
    public boolean isDisabled() {
        return disabled;
    }

    @Override
    public void setDisabled(boolean disabled) {
        if (disabled == this.disabled) return;

        setDisabled(disabled, false, null);
    }

    @Override
    public ActionQueue<ValueChangedEvent<Boolean>> getDisabledChanged() {
        return disabledChanged;
    }

    protected void setDisabled(boolean value, boolean bypassChecks, Bindable<T> source) {

        boolean oldValue = this.disabled;
        disabled = value;

        triggerDisabledChange(oldValue, source != null ? source : this, true, bypassChecks);
    }

    protected void triggerDisabledChange(boolean beforePropagation, Bindable<T> source, boolean propagate, boolean bypassChecks) {
        if (propagate)
            propagateDisabledChanged(source, disabled);

        if (beforePropagation != disabled)
            disabledChanged.execute(new ValueChangedEvent<>(beforePropagation, disabled));
    }

    protected void propagateDisabledChanged(Bindable<T> source, boolean value) {
        gc();

        for (WeakReference<Bindable<T>> binding : bindings) {
            if (binding.refersTo(source)) continue;

            Bindable<T> bindable = binding.get();

            if (bindable == null) continue;

            bindable.setDisabled(value);
        }
    }

    @Override
    public void onDisabledChanged(ValuedAction<Boolean> action, boolean runOnceImmediately) {
        disabledChanged.add(action);

        if (runOnceImmediately)
            action.invoke(new ValueChangedEvent<>(disabled, disabled));
    }

    public void gc() {
        for (WeakReference<Bindable<T>> binding : new ArrayList<>(bindings)) {
            if (binding.refersTo(this)) continue;

            Bindable<T> bindable = binding.get();

            if (bindable == null) bindings.remove(binding);
        }
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

            if (bindable == null) continue;

            unbindWeakFrom(this);
        }

        bindings.clear();
    }

    @Override
    public void unbindBindings() {
        for (WeakReference<Bindable<T>> binding : new ArrayList<>(bindings)) {
            Bindable<T> bindable = binding.get();

            if (bindable == null) continue;

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

    private LeasedBindable<T> leasedBindable;
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
    public ActionQueue<ValueChangedEvent<LeaseState>> getLeaseChanged() {
        return leaseChanged;
    }

    @Override
    public LeasedBindable<T> begin(boolean revertValueOnReturn) {
        if (checkForLease(this))
            throw new IllegalStateException(String.format("Attempted to lease %s, but it was already leased.", this.getClass().getSimpleName()));

        leasedBindable = new LeasedBindable<>(this, revertValueOnReturn);

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
    }

    @Override
    public void onLeaseChanged(Action<ValueChangedEvent<LeaseState>> action, boolean runOnceImmediately) {
        leaseChanged.add(action);

        if (runOnceImmediately)
            action.invoke(new ValueChangedEvent<>(getLeaseState(), getLeaseState()));
    }

    private boolean checkForLease(Bindable<T> source) {
        if (getLeaseState() == LeaseState.LEASED)
            return true;

        if (bindings.isEmpty())
            return false;

        boolean found = false;

        for (WeakReference<Bindable<T>> binding : bindings) {
            if (!binding.refersTo(source)) {
                Bindable<T> bindable = binding.get();

                if (bindable == null) continue;

                found |= bindable.checkForLease(source);
            }
        }

        return found;
    }

    @SuppressWarnings("unchecked")
    public Class<T> getType() {
        if (type == null && value != null)
            type = (Class<T>) value.getClass();

        return type;
    }
}
