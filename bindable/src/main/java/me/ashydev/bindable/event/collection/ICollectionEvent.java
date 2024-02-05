/*
 * Copyright (c) 2024 Ashley (uwuvern) <uwuvern@outlook.com>
 *
 * This project is licensed under the MIT license, check the root of the project for
 * more information.
 */

package me.ashydev.bindable.event.collection;

import java.util.Collection;

public interface ICollectionEvent<E> {
    Type getType();

    Collection<Element<E>> getNew();

    Collection<Element<E>> getOld();

    enum Type {
        ADD,
        REMOVE,
        REPLACE
    }

    class Element<E> {
        private final E value;
        private final int index;

        public Element(E value, int index) {
            this.value = value;
            this.index = index;
        }

        public E getValue() {
            return value;
        }

        public int getIndex() {
            return index;
        }

        @Override
        public String toString() {
            return "Element{" +
                    "value=" + value +
                    ", index=" + index +
                    '}';
        }
    }
}
