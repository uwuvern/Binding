package me.ashydev.bindable.event;

public class ValueChangedEvent<T> {
    public final T old;
    public final T value;

    public ValueChangedEvent(T old, T value) {
        this.old = old;
        this.value = value;
    }
}