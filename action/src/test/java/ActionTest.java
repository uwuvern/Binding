/*
 * Copyright (c) 2024 Ashley (uwuvern) <uwuvern@outlook.com>
 *
 * This project is licensed under the MIT license, check the root of the project for
 * more information.
 */

import me.ashydev.binding.action.Action;
import me.ashydev.binding.action.ValuedAction;
import me.ashydev.binding.common.lang.types.Void;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class ActionTest {

    @Test
    public void testRegularActionInvocation() {
        final AtomicBoolean invoked = new AtomicBoolean(false);

        Action<Void> action = event -> invoked.set(true);

        action.accept(Void.INSTANCE);

        assert invoked.get();
    }

    @Test
    public void testValuedActionInvocation() {
        final AtomicReference<String> invoked = new AtomicReference<>("none");

        ValuedAction<String> action = event -> invoked.set(event.getNew());

        action.accept(invoked.get(), "Hello, World!");

        assert invoked.get().equals("Hello, World!");
    }
}
