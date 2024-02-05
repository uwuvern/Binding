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

public interface IContainer<T> {
    ActionQueue<ValueChangedEvent<T>> getValueChanged();

    T get();

    void set(T value);

    void onValueChanged(ValuedAction<T> action, boolean runOnceImmediately);

    default void onValueChanged(ValuedAction<T> action) {
        onValueChanged(action, false);
    }
}
