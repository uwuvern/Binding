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

public interface IDisableable {
    ValuedActionQueue<Boolean> getDisabledChanged();

    default void disable() {
        setDisabled(true);
    }

    default void enable() {
        setDisabled(false);
    }

    boolean isDisabled();

    void setDisabled(boolean value);

    void onDisabledChanged(ValuedAction<Boolean> action, boolean runOnceImmediately);

    default void onDisabledChanged(ValuedAction<Boolean> action) {
        onDisabledChanged(action, false);
    }
}
