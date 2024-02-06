/*
 * Copyright (c) 2024 Ashley (uwuvern) <uwuvern@outlook.com>
 *
 * This project is licensed under the MIT license, check the root of the project for
 * more information.
 */

import me.ashydev.binding.bindable.Bindable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ThreadedEventTest {
    private Bindable<Integer> bindable;
    private Thread thread;

    private boolean passed = false;

    @BeforeEach
    public void setUp() {
        bindable = new Bindable<>(0);
    }

    @Test
    public void testValueChangedEvent() throws InterruptedException {
        setupFor(TestFor.VALUE_CHANGED);

        thread = new Thread(() -> bindable.set(400));

        thread.start();
        thread.join();

        assert passed;
    }

    @Test
    public void testDisabledChangedEvent() throws InterruptedException {
        setupFor(TestFor.DISABLED_CHANGED);

        thread = new Thread(() -> bindable.setDisabled(true));

        thread.start();
        thread.join();

        assert passed;
    }


    private void setupFor(TestFor testFor) {
        switch (testFor) {
            case VALUE_CHANGED:
                bindable.onValueChanged((event) -> passed = true);
                break;
            case DISABLED_CHANGED:
                bindable.onDisabledChanged((event) -> passed = true);
                break;
        }
    }

    enum TestFor {
        VALUE_CHANGED, DISABLED_CHANGED
    }
}
