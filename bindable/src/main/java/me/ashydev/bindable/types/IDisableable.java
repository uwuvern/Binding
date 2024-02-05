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

public interface IDisableable {
    ActionQueue<ValueChangedEvent<Boolean>> getDisabledChanged();

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
