/*
 * Copyright (c) 2024 Ashley (uwuvern) <uwuvern@outlook.com>
 *
 * This project is licensed under the MIT license, check the root of the project for
 * more information.
 */

package me.ashydev.binding.types;

import me.ashydev.binding.action.ValuedAction;
import me.ashydev.binding.action.event.ValueChangedEvent;
import me.ashydev.binding.action.queue.ActionQueue;
import me.ashydev.binding.action.queue.ValuedActionQueue;

public interface IContainer<T> {
    ValuedActionQueue<T> getValueChanged();

    T get();

    void set(T value);

    void onValueChanged(ValuedAction<T> action, boolean runOnceImmediately);

    default void onValueChanged(ValuedAction<T> action) {
        onValueChanged(action, false);
    }
}
