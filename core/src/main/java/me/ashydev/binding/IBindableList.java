/*
 * Copyright (c) 2024 Ashley (uwuvern) <uwuvern@outlook.com>
 *
 * This project is licensed under the MIT license, check the root of the project for
 * more information.
 */

package me.ashydev.binding;

import me.ashydev.binding.action.Action;
import me.ashydev.binding.action.queue.ActionQueue;
import me.ashydev.binding.event.collection.CollectionEvent;
import me.ashydev.binding.types.IBindingContainer;
import me.ashydev.binding.types.ICopyable;
import me.ashydev.binding.types.IDisableable;
import me.ashydev.binding.types.InstanceContainer;

import java.io.Serializable;
import java.util.List;

public interface IBindableList<T>
        extends List<T>, IDisableable, ICopyable<IBindableList<T>>,
        IUnbindable, InstanceContainer<IBindableList<T>>, IBindingContainer<IBindableList<T>>,
        Serializable {
    static <T, V extends IBindableList<T>> IBindableList<T> create(V source) {
        IBindableList<T> copy = source.createInstance();

        if (copy.getClass() != source.getClass()) {
            throw new IllegalArgumentException(
                    String.format("Attempted to create a bindable copy of %s, but the returned type was %s", source.getClass().getSimpleName(), copy.getClass().getSimpleName())
                            + String.format("Override %s.createInstance() for GetBoundCopy() to return the correct type.", source.getClass().getSimpleName())
            );
        }

        return copy.bindTo(source);
    }

    static <T, V extends IBindableList<T>> IBindableList<T> createWeak(V source) {
        IBindableList<T> copy = source.createInstance();

        if (copy.getClass() != source.getClass()) {
            throw new IllegalArgumentException(
                    String.format("Attempted to create a bindable copy of %s, but the returned type was %s", source.getClass().getSimpleName(), copy.getClass().getSimpleName())
                            + String.format("Override %s.createInstance() for GetBoundCopy() to return the correct type.", source.getClass().getSimpleName())
            );
        }

        return copy.weakBind(source);
    }

    void onCollectionChanged(Action<CollectionEvent<T>> action, boolean runOnceImmediately);
    default void onCollectionChanged(Action<CollectionEvent<T>> action) {
        onCollectionChanged(action, false);
    }

    ActionQueue<CollectionEvent<T>> getCollectionChanged();
}
