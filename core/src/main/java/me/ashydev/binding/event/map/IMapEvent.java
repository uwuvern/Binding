/*
 * Copyright (c) 2024 Ashley (uwuvern) <uwuvern@outlook.com>
 *
 * This project is licensed under the MIT license, check the root of the project for
 * more information.
 */

package me.ashydev.binding.event.map;

import java.util.Collection;

public interface IMapEvent<K, E> {
    Collection<Element<K, E>> getNew();
    Collection<Element<K, E>> getOld();

    Type getType();

    enum Type {
        ADD, REMOVE, REPLACE
    }

    record Element<K, E>(K key, E element) {

        @Override
            public String toString() {
                return "Element{" +
                        "key=" + key +
                        ", element=" + element +
                        '}';
            }
        }
}
