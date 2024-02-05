package me.ashydev.bindable;

import me.ashydev.bindable.bindable.LeasedBindable;
import me.ashydev.bindable.bindable.precision.PrecisionBindable;
import me.ashydev.bindable.bindable.precision.PrecisionConstrainedBindable;

public class Main {

    public static void main(String[] args) {
        final PrecisionBindable<Integer> bindable = new PrecisionBindable<>(5, 0.1f);

        final LeasedBindable<Integer> leased = bindable.begin();


    }
}
