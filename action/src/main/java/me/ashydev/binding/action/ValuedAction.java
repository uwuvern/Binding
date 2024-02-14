/*
 * Copyright (c) 2024 Ashley (uwuvern) <uwuvern@outlook.com>
 *
 * This project is licensed under the MIT license, check the root of the project for
 * more information.
 */

package me.ashydev.binding.action;

import me.ashydev.binding.action.event.ValueChangedEvent;

@FunctionalInterface
public interface ValuedAction<T> extends Action<ValueChangedEvent<T>> {

    static <T> ValuedAction<T> empty() {
        return event -> {
        };
    }

    static <T> ValueChangedEvent<T> event(T old, T next) {
        return new ValueChangedEvent<>(old, next);
    }

    default void accept(T old, T next) {
        accept(event(old, next));
    }
}



