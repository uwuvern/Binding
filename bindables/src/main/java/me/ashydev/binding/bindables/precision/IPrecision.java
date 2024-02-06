/*
 * Copyright (c) 2024 Ashley (uwuvern) <uwuvern@outlook.com>
 *
 * This project is licensed under the MIT license, check the root of the project for
 * more information.
 */

package me.ashydev.binding.bindables.precision;

import me.ashydev.binding.action.ValuedAction;
import me.ashydev.binding.action.queue.ValuedActionQueue;

public interface IPrecision<T extends Number> {
    ValuedActionQueue<T> getPrecisionChanged();

    ValuedActionQueue<T> getDefaultPrecisionChanged();

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
