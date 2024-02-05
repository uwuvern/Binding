/*
 * Copyright (c) 2024 Ashley (uwuvern) <uwuvern@outlook.com>
 *
 * This project is licensed under the MIT license, check the root of the project for
 * more information.
 */

package me.ashydev.bindable.bindable.precision;

public class PrecisionBindable<T extends Number> extends PrecisionConstrainedBindable<T, Float> {
    public PrecisionBindable(T value, float precision) {
        super(value, precision);
    }

    public PrecisionBindable(float precision) {
        super(precision);
    }
}
