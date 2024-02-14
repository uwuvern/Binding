package me.ashydev.binding.bindables.rollover;

import me.ashydev.binding.bindables.ranged.IMinMax;

public interface Rollable<T> {
    void set(int index);
    T get(int index);

    int min();
    int max();

    int index();

    default void increment(int amount) {
        set(index() + amount);
    }

    default void decrement(int amount) {
        set(index() - amount);
    }

    default void increment() {
        increment(1);
    }

    default void decrement() {
        decrement(1);
    }

    default int recalculate(int index) {
        final double max = max() - 1, min = min() - 1;

        if (index <= max && index >= min) return index;

        return (int) Math.round(min + ((index - min) - (Math.floor((index - min) / (max - min)) * (max - min))));
    }
}
