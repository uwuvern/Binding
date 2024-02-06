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

public class ThreadedBindingTest {
    private Bindable<Integer> bindable, other;
    private Thread thread;

    @BeforeEach
    public void setUp() {
        bindable = new Bindable<>(0);
        other = new Bindable<>(0);
    }

    @Test
    public void testBind() throws InterruptedException {
        thread = new Thread(() -> {
            other.bindTo(bindable);
            bindable.set(400);
        });

        thread.start();
        thread.join();

        assert other.get() == 400 && bindable.get() == 400;
    }

    @Test
    public void testWeakBind() throws InterruptedException {
        thread = new Thread(() -> {
            other.weakBind(bindable);
            bindable.set(400);
        });

        thread.start();
        thread.join();

        assert other.get() == 400 && bindable.get() == 400;
    }

    @Test
    public void testUnbind() throws InterruptedException {
        thread = new Thread(() -> {
            other.bindTo(bindable);
            bindable.set(400);
            other.unbind();
            bindable.set(200);
        });

        thread.start();
        thread.join();

        assert other.get() == 400 && bindable.get() == 200;
    }

    @Test
    public void testWeakImmutability() throws InterruptedException {
        thread = new Thread(() -> {
            other.weakBind(bindable);
            bindable.set(400);
            other.set(200);
        });

        thread.start();
        thread.join();

        assert bindable.get() == 400 && other.get() == 200;
    }

    @Test
    public void testBindMutability() throws InterruptedException {
        thread = new Thread(() -> {
            other.bindTo(bindable);
            bindable.set(400);
            other.set(200);
        });

        thread.start();
        thread.join();

        assert bindable.get() == 200 && other.get() == 200;
    }
}
