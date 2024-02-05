package me.ashydev.bindable;

public interface IUnbindable {
    void unbindEvents();

    void unbindWeak();

    void unbindBindings();

    void unbind();

    void unbindFrom(IUnbindable other);

    void unbindWeakFrom(IUnbindable other);
}
