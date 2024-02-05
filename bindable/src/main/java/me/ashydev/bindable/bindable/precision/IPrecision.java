/*
 * Copyright (c) 2024 Ashley (uwuvern) <uwuvern@outlook.com>
 *
 * This project is licensed under the MIT license, check the root of the project for
 * more information.
 */

package me.ashydev.bindable.bindable.precision;

import me.ashydev.bindable.action.ValuedAction;
import me.ashydev.bindable.action.queue.ActionQueue;
import me.ashydev.bindable.event.ValueChangedEvent;

import java.util.List;

public interface IPrecision<T extends Number> {
    ActionQueue<ValueChangedEvent<T>> getPrecisionChanged();
    ActionQueue<ValueChangedEvent<T>> getDefaultPrecisionChanged();

    T getPrecision();
    void setPrecision(T precision);

    T getDefaultPrecision();
    void setDefaultPrecision(T precision);

    void onPrecisionChanged(ValuedAction<T> action, boolean runOnceImmediately);
    default void onPrecisionChanged(ValuedAction<T> action) {
        onPrecisionChanged(action, false);
    }

    void onDefaultPrecisionChanged(ValuedAction<T> action, boolean runOnceImmediately);
    default void onDefaultPrecisionChanged(ValuedAction<T> action) {
        onDefaultPrecisionChanged(action, false);
    }
}
