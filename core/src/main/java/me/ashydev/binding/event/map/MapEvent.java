/*
 * Copyright (c) 2024 Ashley (uwuvern) <uwuvern@outlook.com>
 *
 * This project is licensed under the MIT license, check the root of the project for
 * more information.
 */

package me.ashydev.binding.event.map;

import java.util.Collection;

public class MapEvent<K, E> implements IMapEvent<K, E> {
    private final Type type;
    private final Collection<Element<K, E>> newElements;
    private final Collection<Element<K, E>> oldElements;

    public MapEvent(Type type, Collection<Element<K, E>> newElements, Collection<Element<K, E>> oldElements) {
        this.type = type;
        this.newElements = newElements;
        this.oldElements = oldElements;
    }

    @Override
    public Collection<Element<K, E>> getNew() {
        return newElements;
    }

    @Override
    public Collection<Element<K, E>> getOld() {
        return oldElements;
    }

    @Override
    public Type getType() {
        return type;
    }

    @Override
    public String toString() {
        return "MapEvent{" +
                "type=" + type +
                ", newElements=" + newElements +
                ", oldElements=" + oldElements +
                '}';
    }
}
