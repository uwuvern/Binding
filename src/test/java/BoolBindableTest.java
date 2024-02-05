/*
 * Copyright (c) 2024 Ashley (uwuvern) <uwuvern@outlook.com>
 *
 * This project is licensed under the MIT license, check the root of the project for
 * more information.
 */

import me.ashydev.bindable.bindable.base.primitive.BindableBool;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BoolBindableTest {
    private BindableBool bindable;

    @BeforeEach
    public void setUp() {
        bindable = new BindableBool(true);
    }

    @Test
    public void testToggle() {
        bindable.toggle();

        assert !bindable.get();
    }

    @Test
    public void testSet() {
        bindable.set(false);

        assert !bindable.get();
    }

    @Test
    public void testOn() {
        bindable.on();

        assert bindable.get();
    }

    @Test
    public void testOff() {
        bindable.off();

        assert !bindable.get();
    }
}
