/*
 * Copyright (c) 2024 Ashley (uwuvern) <uwuvern@outlook.com>
 *
 * This project is licensed under the MIT license, check the root of the project for
 * more information.
 */

package me.ashydev.binding.bindables.ranged;

import me.ashydev.binding.action.ValuedAction;
import me.ashydev.binding.action.queue.ValuedActionQueue;

public interface IMinMax<T extends Number> {
    ValuedActionQueue<T> getOnMinChanged();
    ValuedActionQueue<T> getOnMaxChanged();

    ValuedActionQueue<T> getOnDefaultMinChanged();
    ValuedActionQueue<T> getOnDefaultMaxChanged();


    T getMin();
    T getMax();

    void setMin(T min);
    void setMax(T max);

    T getDefaultMin();
    T getDefaultMax();

    void setDefaultMin(T min);
    void setDefaultMax(T max);

    float normalized();

    boolean hasDefinedRange();

    void onMinChanged(ValuedAction<T> action, boolean runOnceImmediately);
    default void onMinChanged(ValuedAction<T> action) {
        onMinChanged(action, false);
    }

    void onMaxChanged(ValuedAction<T> action, boolean runOnceImmediately);
    default void onMaxChanged(ValuedAction<T> action) {
        onMaxChanged(action, false);
    }

    void onDefaultMinChanged(ValuedAction<T> action, boolean runOnceImmediately);
    default void onDefaultMinChanged(ValuedAction<T> action) {
        onDefaultMinChanged(action, false);
    }

    void onDefaultMaxChanged(ValuedAction<T> action, boolean runOnceImmediately);
    default void onDefaultMaxChanged(ValuedAction<T> action) {
        onDefaultMaxChanged(action, false);
    }
}
