/*
 * Copyright (c) 2024 Ashley (uwuvern) <uwuvern@outlook.com>
 *
 * This project is licensed under the MIT license, check the root of the project for
 * more information.
 */

package me.ashydev.binding.types;

import me.ashydev.binding.action.ValuedAction;
import me.ashydev.binding.action.queue.ValuedActionQueue;

public interface ILeaser<T> {
    ValuedActionQueue<LeaseState> getLeaseChanged();

    T begin(boolean revertValueOnReturn);

    default T begin() {
        return begin(true);
    }

    void end(T bindable);

    void onLeaseChanged(ValuedAction<LeaseState> action, boolean runOnceImmediately);

    default void onLeaseChanged(ValuedAction<LeaseState> action) {
        onLeaseChanged(action, false);
    }

    enum LeaseState {
        LEASED,
        RETURNED,
        NONE
    }

}
