/*
 * Copyright (c) 2024 Ashley (uwuvern) <uwuvern@outlook.com>
 *
 * This project is licensed under the MIT license, check the root of the project for
 * more information.
 */

package me.ashydev.binding.action.queue;

import me.ashydev.binding.action.Action;
import me.ashydev.binding.common.lang.types.Void;

import java.util.Collection;

public class VoidActionQueue extends ActionQueue<Void> {
    public VoidActionQueue() {
        super();
    }

    public VoidActionQueue(int numElements) {
        super(numElements);
    }

    public VoidActionQueue(Collection<? extends Action<Void>> c) {
        super(c);
    }
}
