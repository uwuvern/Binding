/*
 * Copyright (c) 2024 Ashley (uwuvern) <uwuvern@outlook.com>
 *
 * This project is licensed under the MIT license, check the root of the project for
 * more information.
 */

package me.ashydev.binding;

import me.ashydev.binding.action.Action;
import me.ashydev.binding.action.queue.ActionQueue;
import me.ashydev.binding.event.map.MapEvent;
import me.ashydev.binding.types.IBindingContainer;
import me.ashydev.binding.types.ICopyable;
import me.ashydev.binding.types.IDisableable;
import me.ashydev.binding.types.InstanceContainer;

import java.io.Serializable;
import java.util.Map;

public interface IBindableMap<K, V>
        extends Map<K, V>, IDisableable, ICopyable<IBindableMap<K, V>>,
        IUnbindable, InstanceContainer<IBindableMap<K, V>>,
        IBindingContainer<IBindableMap<K, V>>, Serializable {
    static <K, V, M extends IBindableMap<K, V>> IBindableMap<K, V> create(M source) {
        IBindableMap<K, V> copy = source.createInstance();

        if (copy.getClass() != source.getClass()) {
            throw new IllegalArgumentException(
                    String.format("Attempted to create a bindable copy of %s, but the returned type was %s", source.getClass().getSimpleName(), copy.getClass().getSimpleName())
                            + String.format("Override %s.createInstance() for GetBoundCopy() to return the correct type.", source.getClass().getSimpleName())
            );
        }

        return copy.bindTo(source);
    }

    static <K, V, M extends IBindableMap<K, V>> IBindableMap<K, V> createWeak(M source) {
        IBindableMap<K, V> copy = source.createInstance();

        if (copy.getClass() != source.getClass()) {
            throw new IllegalArgumentException(
                    String.format("Attempted to create a bindable copy of %s, but the returned type was %s", source.getClass().getSimpleName(), copy.getClass().getSimpleName())
                            + String.format("Override %s.createInstance() for GetBoundCopy() to return the correct type.", source.getClass().getSimpleName())
            );
        }

        return copy.weakBind(source);
    }

    void onCollectionChanged(Action<MapEvent<K, V>> action, boolean runOnceImmediately);
    default void onCollectionChanged(Action<MapEvent<K, V>> action) {
        onCollectionChanged(action, false);
    }

    ActionQueue<MapEvent<K, V>> getCollectionChanged();
}
