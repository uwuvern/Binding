package me.ashydev.bindable;

import me.ashydev.bindable.bindable.Bindable;
import me.ashydev.bindable.bindable.LeasedBindable;
import me.ashydev.bindable.bindable.precision.PrecisionBindable;
import me.ashydev.bindable.bindable.precision.PrecisionConstrainedBindable;

public class Main {

    public static void main(String[] args) {
        final Bindable<Integer> bindable = new Bindable<>(0);
        final Bindable<Integer> watcher = bindable.getWeakCopy();

        watcher.onValueChanged((event) -> {
            System.out.println("Watcher: " + event);
        });

        bindable.set(1);

    }
}
