/*
 * Copyright (c) 2024 Ashley (uwuvern) <uwuvern@outlook.com>
 *
 * This project is licensed under the MIT license, check the root of the project for
 * more information.
 */

package me.ashydev.binding.action.event;

public class ValueChangedEvent<T> {
    public final T old;
    public final T value;

    public ValueChangedEvent(T old, T value) {
        this.old = old;
        this.value = value;
    }

    public T getOld() {
        return old;
    }

    public T getNew() {
        return value;
    }

    @Override
    public String toString() {
        return "ValueChangedEvent{" +
                "old=" + old +
                ", value=" + value +
                '}';
    }
}