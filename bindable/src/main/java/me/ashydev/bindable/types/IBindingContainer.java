/*
 * Copyright (c) 2024 Ashley (uwuvern) <uwuvern@outlook.com>
 *
 * This project is licensed under the MIT license, check the root of the project for
 * more information.
 */

package me.ashydev.bindable.types;

public interface IBindingContainer<T> {
    T bindTo(T other);

    T weakBind(T other);
}
