/*
 * Copyright (c) 2024 Ashley (uwuvern) <uwuvern@outlook.com>
 *
 * This project is licensed under the MIT license, check the root of the project for
 * more information.
 */

package me.ashydev.bindable.bindable.ranged;

public interface IMinMax<T extends Number> {

    T getMin();
    T getMax();

    void setMin(T min);
    void setMax(T max);

    T getDefaultMin();
    T getDefaultMax();

    void setDefaultMin(T min);
    void setDefaultMax(T max);

    float normalized();

    boolean hasDefinedRange();


}
