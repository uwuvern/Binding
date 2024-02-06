/*
 * Copyright (c) 2024 Ashley (uwuvern) <uwuvern@outlook.com>
 *
 * This project is licensed under the MIT license, check the root of the project for
 * more information.
 */

package me.ashydev.binding.action.queue;

import me.ashydev.binding.action.Action;
import me.ashydev.binding.action.event.ValueChangedEvent;

import java.util.Collection;

public class ValuedActionQueue<T> extends ActionQueue<ValueChangedEvent<T>> {
    public ValuedActionQueue() {
        super();
    }

    public ValuedActionQueue(int numElements) {
        super(numElements);
    }

    public ValuedActionQueue(Collection<? extends Action<ValueChangedEvent<T>>> c) {
        super(c);
    }
}
