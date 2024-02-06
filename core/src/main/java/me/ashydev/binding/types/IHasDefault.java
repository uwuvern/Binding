/*
 * Copyright (c) 2024 Ashley (uwuvern) <uwuvern@outlook.com>
 *
 * This project is licensed under the MIT license, check the root of the project for
 * more information.
 */

package me.ashydev.binding.types;

import me.ashydev.binding.action.ValuedAction;
import me.ashydev.binding.action.queue.ValuedActionQueue;

public interface IHasDefault<T> {
    ValuedActionQueue<T> getDefaultChanged();

    T getDefaultValue();

    void setDefaultValue(T value);

    void setDefault();

    boolean isDefault();

    void onDefaultChanged(ValuedAction<T> action, boolean runOnceImmediately);

    default void onDefaultChanged(ValuedAction<T> action) {
        onDefaultChanged(action, false);
    }
}
