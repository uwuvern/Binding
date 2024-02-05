/*
 * Copyright (c) 2024 Ashley (uwuvern) <uwuvern@outlook.com>
 *
 * This project is licensed under the MIT license, check the root of the project for
 * more information.
 */

package me.ashydev.bindable.types;

import me.ashydev.bindable.action.Action;
import me.ashydev.bindable.action.ValuedAction;
import me.ashydev.bindable.action.queue.ActionQueue;
import me.ashydev.bindable.event.ValueChangedEvent;

public interface IHasDefault<T> {
    ActionQueue<ValueChangedEvent<T>> getDefaultChanged();

    T getDefaultValue();

    void setDefaultValue(T value);

    void setDefault();

    boolean isDefault();

    void onDefaultChanged(ValuedAction<T> action, boolean runOnceImmediately);

    default void onDefaultChanged(ValuedAction<T> action) {
        onDefaultChanged(action, false);
    }
}
