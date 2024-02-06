/*
 * Copyright (c) 2024 Ashley (uwuvern) <uwuvern@outlook.com>
 *
 * This project is licensed under the MIT license, check the root of the project for
 * more information.
 */

package me.ashydev.binding.action;

import me.ashydev.binding.common.lang.types.Void;

public interface VoidAction extends Action<Void> {
    void invoke();

    @Override
    default void invoke(Void value) {
        invoke();
    }
}
