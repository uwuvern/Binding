/*
 * Copyright (c) 2024 Ashley (uwuvern) <uwuvern@outlook.com>
 *
 * This project is licensed under the MIT license, check the root of the project for
 * more information.
 */

package me.ashydev.bindable.bindable.precision;

import me.ashydev.bindable.IBindable;
import me.ashydev.bindable.bindable.Bindable;

public class PrecisionBindable<T extends Number> extends PrecisionConstrainedBindable<T, Float> {
    public PrecisionBindable(T value, float precision) {
        super(value, precision);
    }

    public PrecisionBindable(float precision) {
        super(precision);
    }

    public PrecisionBindable() {
        super(0.01f);
    }

    @Override
    public PrecisionBindable<T> createInstance() {
        return new PrecisionBindable<>();
    }
}
