/*
 * Copyright (c) 2024 Ashley (uwuvern) <uwuvern@outlook.com>
 *
 * This project is licensed under the MIT license, check the root of the project for
 * more information.
 */

package me.ashydev.binding.bindables.primitive;

import me.ashydev.binding.bindable.Bindable;

public class BindableBool extends Bindable<Boolean> implements IToggleable {
    public BindableBool() {
        super();
    }

    public BindableBool(Object input) {
        super();
        boolean value;

        switch (input) {
            case String s -> value = Boolean.parseBoolean(s);
            case Number number -> value = number.intValue() != 0;
            case Boolean b -> value = b;
            case null, default -> throw new IllegalArgumentException("Invalid input type for BindableBool");
        }

        set(value);
    }

    public BindableBool(boolean value) {
        super(value);
    }

    @Override
    public Bindable<Boolean> createInstance() {
        return new BindableBool();
    }

    @Override
    public void toggle() {
        set(!get());
    }

    @Override
    public void on() {
        set(true);
    }

    @Override
    public void off() {
        set(false);
    }
}
