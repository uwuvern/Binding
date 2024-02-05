/*
 * Copyright (c) 2024 Ashley (uwuvern) <uwuvern@outlook.com>
 *
 * This project is licensed under the MIT license, check the root of the project for
 * more information.
 */

package me.ashydev.bindable.action;

import me.ashydev.bindable.event.ValueChangedEvent;

public interface ValuedAction<T> extends Action<ValueChangedEvent<T>> { }
