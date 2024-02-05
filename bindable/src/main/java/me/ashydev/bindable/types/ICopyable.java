package me.ashydev.bindable.types;

public interface ICopyable<T> {
    T copy();

    T copyTo(T other);

    T getBoundCopy();

    T getUnboundCopy();

    T getWeakCopy();
}
