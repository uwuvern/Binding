package me.ashydev.bindable.types;

public interface IBindingContainer<T> {
    T bindTo(T other);

    T weakBind(T other);
}
