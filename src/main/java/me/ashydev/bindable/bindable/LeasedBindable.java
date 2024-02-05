/*
 * Copyright (c) 2024 Ashley (uwuvern) <uwuvern@outlook.com>
 *
 * This project is licensed under the MIT license, check the root of the project for
 * more information.
 */

package me.ashydev.bindable.bindable;

import me.ashydev.bindable.ILeasedBindable;
import me.ashydev.bindable.types.ILeased;

public class LeasedBindable<T> extends Bindable<T> implements ILeasedBindable<T> {
    private final Bindable<T> source;

    private final T valueBeforeLease;
    private final boolean disabledBeforeLease;
    private final boolean revertValueOnReturn;


    public LeasedBindable(Bindable<T> source, boolean revertValueOnReturn) {
        bindTo(source);

        if (source == null)
            throw new IllegalArgumentException("source was null, cannot lease a null source.");

        this.source = source;
        this.revertValueOnReturn = revertValueOnReturn;

        if (revertValueOnReturn) this.valueBeforeLease = source.get();
        else this.valueBeforeLease = null;

        disabledBeforeLease = disabled;

        disabled = true;
    }

    private LeasedBindable(T value) {
        super(value);

        source = null;
        valueBeforeLease = null;
        disabledBeforeLease = false;
        revertValueOnReturn = false;
    }

    private LeasedBindable() {
        super();

        source = null;
        valueBeforeLease = null;
        disabledBeforeLease = false;
        revertValueOnReturn = false;
    }

    private boolean hasBeenReturned = false;

    @Override
    public boolean lease() {
        if (hasBeenReturned) return false;

        if (source == null)
            throw new IllegalStateException(String.format("Must return a leased bindable before attempting to lease it again. %s has already been returned.", this));

        unbind();

        return true;
    }

    @Override
    public void end() {
        if (source == null)
            throw new IllegalStateException(String.format("Cannot end a %s that has not been leased.", getClass().getSimpleName()));

        unbind();
    }

    @Override
    public void set(T value) {
        if (source != null)
            checkValid();

        if (this.value != null && this.value.equals(value)) return;

        updateValue(value, true, null);
    }

    @Override
    public void unbind() {
        if (source != null && !hasBeenReturned) {
            if (revertValueOnReturn) set(valueBeforeLease);

            disabled = disabledBeforeLease;

            source.end(this);
            hasBeenReturned = true;
        }

        super.unbind();
    }

    @Override
    public LeasedBindable<T> createInstance() {
        return new LeasedBindable<>();
    }

    private void checkValid() {
        if (source != null && hasBeenReturned)
            throw new IllegalStateException(String.format("Cannot perform any operations on a %s that has been returned.", getClass().getSimpleName()));
    }
}
