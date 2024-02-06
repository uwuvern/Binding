/*
 * Copyright (c) 2024 Ashley (uwuvern) <uwuvern@outlook.com>
 *
 * This project is licensed under the MIT license, check the root of the project for
 * more information.
 */

import me.ashydev.binding.bindable.Bindable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CrossThreadBindingTest {
    private Bindable<Integer> bindable, other;
    private Thread thread1, thread2;

    @BeforeEach
    public void setUp() {
        bindable = new Bindable<>(0);
        other = new Bindable<>(0);
    }

    @Test
    public void testBind() throws InterruptedException {
        thread1 = new Thread(() -> {
            other.bindTo(bindable);
            bindable.set(400);
        });

        thread2 = new Thread(() -> {
            bindable.onValueChanged((event) -> {
                assert other.get() == 400;
            });
        });

        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();
    }

    @Test
    public void testWeakBind() throws InterruptedException {
        thread1 = new Thread(() -> {
            other.weakBind(bindable);
            bindable.set(400);
        });

        thread2 = new Thread(() -> {
            bindable.onValueChanged((event) -> {
                assert other.get() == 400;
            });
        });

        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();
    }

    @Test
    public void testUnbind() throws InterruptedException {
        thread1 = new Thread(() -> {
            other.bindTo(bindable);
            bindable.set(400);
            other.unbind();
            bindable.set(200);
        });

        thread2 = new Thread(() -> {
            bindable.onValueChanged((event) -> {
                assert other.get() == 400;
            });
            other.onValueChanged((event) -> {
                assert other.get() == 400 || other.get() == 200;
            });
        });

        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();
    }
}
