/*
 * Copyright (c) 2024 Ashley (uwuvern) <uwuvern@outlook.com>
 *
 * This project is licensed under the MIT license, check the root of the project for
 * more information.
 */

package me.ashydev.binding.event.collection;

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

    record Element<E>(E value, int index) {

        @Override
        public String toString() {
            return "Element{" +
                    "value=" + value +
                    ", index=" + index +
                    '}';
        }
    }
}
