/*
 * Copyright (c) 2024 Ashley (uwuvern) <uwuvern@outlook.com>
 *
 * This project is licensed under the MIT license, check the root of the project for
 * more information.
 */

import me.ashydev.binding.common.reference.LockedWeakList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.ref.WeakReference;

public class LockedWeakListTest {
    private LockedWeakList<Integer> list;

    @BeforeEach
    public void setUp() {
        list = new LockedWeakList<>();
    }

    @Test
    public void testThreadSafety() {
        Thread thread = new Thread(() -> {
            WeakReference<Integer> reference = new WeakReference<>(25);

            list.add(reference);
        });

        thread.start();

        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assert list.size() == 1;
    }
}
