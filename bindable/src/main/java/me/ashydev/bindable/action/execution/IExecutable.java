package me.ashydev.bindable.action.execution;

public interface IExecutable<T> {
    boolean execute(T event);
}
