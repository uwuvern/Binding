/*
 * Copyright (c) 2024 Ashley (uwuvern) <uwuvern@outlook.com>
 *
 * This project is licensed under the MIT license, check the root of the project for
 * more information.
 */

package me.ashydev.binding;

public interface IUnbindable {
    void unbindEvents();

    void unbindWeak();

    void unbindBindings();

    void unbind();

    void unbindFrom(IUnbindable other);

    void unbindWeakFrom(IUnbindable other);
}
