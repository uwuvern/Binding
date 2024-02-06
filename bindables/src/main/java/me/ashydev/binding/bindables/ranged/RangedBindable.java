/*
 * Copyright (c) 2024 Ashley (uwuvern) <uwuvern@outlook.com>
 *
 * This project is licensed under the MIT license, check the root of the project for
 * more information.
 */

package me.ashydev.binding.bindables.ranged;

public class RangedBindable<T extends Number> extends RangeConstrainedBindable<T> {
    public RangedBindable(T value, T min, T max) {
        super(value, min, max);
    }

    public RangedBindable(T min, T max) {
        super(min, max);
    }

    @Override
    public RangedBindable<T> createInstance() {
        return new RangedBindable<>(getMin(), getMax());
    }
}
