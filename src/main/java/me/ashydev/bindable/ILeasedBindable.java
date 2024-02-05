/*
 * Copyright (c) 2024 Ashley (uwuvern) <uwuvern@outlook.com>
 *
 * This project is licensed under the MIT license, check the root of the project for
 * more information.
 */

package me.ashydev.bindable;

import me.ashydev.bindable.bindable.Bindable;
import me.ashydev.bindable.types.ILeased;

public interface ILeasedBindable<T> extends ILeased, IBindable<T> { }
