package me.ashydev.binding.bindables.enumeration;

import me.ashydev.binding.bindables.rollover.Rollable;

public interface Enumerable<E extends Enum<E>> extends Rollable<E> {
    void set(String name);
    void set(int index);

    E lookup(String name);
    E lookup(int index);

    @Override
    default E get(int index) {
        return lookup(index);
    }
    int ordinal();

    @Override
    default int index() {
        return ordinal();
    }
    E[] getConstants();

    @Override
    default int min() {
        return 0;
    }

    @Override
    default int max() {
        return getConstants().length;
    }
}
