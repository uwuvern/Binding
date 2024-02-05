package me.ashydev.bindable.action;

@FunctionalInterface
public interface Action<T> {
    void invoke(T event);
}
