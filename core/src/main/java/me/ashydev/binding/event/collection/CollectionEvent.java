/*
 * Copyright (c) 2024 Ashley (uwuvern) <uwuvern@outlook.com>
 *
 * This project is licensed under the MIT license, check the root of the project for
 * more information.
 */

package me.ashydev.binding.event.collection;

import java.util.Collection;

public class CollectionEvent<E> implements ICollectionEvent<E> {
    private final Type type;
    private final Collection<Element<E>> newElements;
    private final Collection<Element<E>> oldElements;

    public CollectionEvent(Type type, Collection<Element<E>> newElements, Collection<Element<E>> oldElements) {
        this.type = type;
        this.newElements = newElements;
        this.oldElements = oldElements;
    }


    @Override
    public Type getType() {
        return type;
    }

    @Override
    public Collection<Element<E>> getNew() {
        return newElements;
    }

    @Override
    public Collection<Element<E>> getOld() {
        return oldElements;
    }

    @Override
    public String toString() {
        return "CollectionEvent{" +
                "type=" + type +
                ", newElements=" + newElements +
                ", oldElements=" + oldElements +
                '}';
    }
}
