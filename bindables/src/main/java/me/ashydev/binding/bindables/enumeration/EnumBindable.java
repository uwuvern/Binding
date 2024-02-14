package me.ashydev.binding.bindables.enumeration;

import me.ashydev.binding.bindable.Bindable;

public class EnumBindable<E extends Enum<E>> extends Bindable<E> implements Enumerable<E> {

    public EnumBindable() {
        super();
    }

    public EnumBindable(E value) {
        super(value);
    }

    @Override
    public void set(String name) {
        set(lookup(name));
    }

    @Override
    public void set(int index) {
        set(lookup(index));
    }

    @Override
    public E lookup(String name) {
        for (E constant : getConstants()) {
            if (constant.name().equals(name)) {
                return constant;
            }
        }

        return null;
    }

    @Override
    public E lookup(int index) {
        int recalculated = recalculate(index);

        System.out.println("Recalculated: " + recalculated);

        return getConstants()[recalculate(index)];
    }

    @Override
    public int ordinal() {
        return value.ordinal();
    }

    @Override
    public E[] getConstants() {
        return value
                .getDeclaringClass()
                .getEnumConstants();
    }
}
