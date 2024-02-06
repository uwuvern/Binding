/*
 * Copyright (c) 2024 Ashley (uwuvern) <uwuvern@outlook.com>
 *
 * This project is licensed under the MIT license, check the root of the project for
 * more information.
 */

package me.ashydev.binding.types;

public interface ICopyable<T> {
    T copy();

    T copyTo(T other);

    T getBoundCopy();

    T getUnboundCopy();

    T getWeakCopy();
}
