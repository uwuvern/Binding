/*
 * Copyright (c) 2024 Ashley (uwuvern) <uwuvern@outlook.com>
 *
 * This project is licensed under the MIT license, check the root of the project for
 * more information.
 */

package me.ashydev.binding.kotlin.types

import me.ashydev.binding.types.IContainer

var <T> IContainer<T>.value: T
    get() = get()
    set(value) = set(value)
