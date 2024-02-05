/*
 * Copyright (c) 2024 Ashley (uwuvern) <uwuvern@outlook.com>
 *
 * This project is licensed under the MIT license, check the root of the project for
 * more information.
 */

package me.ashydev.bindable.bindable.map;

import me.ashydev.bindable.IBindableList;
import me.ashydev.bindable.IBindableMap;
import me.ashydev.bindable.IUnbindable;
import me.ashydev.bindable.action.Action;
import me.ashydev.bindable.action.ValuedAction;
import me.ashydev.bindable.action.queue.ActionQueue;
import me.ashydev.bindable.event.ValueChangedEvent;
import me.ashydev.bindable.event.collection.CollectionEvent;
import me.ashydev.bindable.event.map.IMapEvent;
import me.ashydev.bindable.event.map.MapEvent;
import me.ashydev.bindable.reference.LockedWeakList;
import java.lang.ref.WeakReference;
import java.util.*;
import java.util.function.UnaryOperator;

public class BindableMap<K, V> implements IBindableMap<K, V> {
    private transient final WeakReference<BindableMap<K, V>> weakReference = new WeakReference<>(this);

    private transient final ActionQueue<MapEvent<K, V>> collectionChanged = new ActionQueue<>();
    private transient final ActionQueue<ValueChangedEvent<Boolean>> disabledChanged = new ActionQueue<>();

    private transient final LockedWeakList<BindableMap<K, V>> bindings = new LockedWeakList<>();
    private transient boolean disabled;

    private final Map<K, V> map;


    public BindableMap(MapType type, Map<K, V> items) {
        this.map = switch (type) {
            case HASH -> new HashMap<>();
            case LINKED -> new LinkedHashMap<>();
            case IDENTITY -> new IdentityHashMap<>();
            case WEAK -> new WeakHashMap<>();
        };

        if (items != null)
            map.putAll(items);

        this.disabled = false;
    }

    public BindableMap() {
        this(MapType.HASH, null);
    }

    @Override
    public void onCollectionChanged(Action<MapEvent<K, V>> action, boolean runOnceImmediately) {
        collectionChanged.add(action);

        if (runOnceImmediately)
            action.invoke(
                    new MapEvent<>(
                            MapEvent.Type.ADD,
                            getElements(map),
                            Collections.emptyList()
                    )
            );
    }

    @Override
    public BindableMap<K, V> createInstance() {
        return new BindableMap<>();
    }

    private boolean checkAlreadyApplied(Set<BindableMap<K, V>> appliedInstances) {
        if (appliedInstances.contains(this))
            return true;

        appliedInstances.add(this);
        return false;
    }

    private void ensureMutationAllowed() {
        if (isDisabled())
            throw new IllegalStateException(String.format("Cannot mutate the %s while it is disabled.", getClass().getSimpleName()));
    }


    @Override
    public ActionQueue<ValueChangedEvent<Boolean>> getDisabledChanged() {
        return disabledChanged;
    }

    protected void setDisabled(boolean value, boolean bypassChecks, BindableMap<K, V> source) {
        boolean oldValue = this.disabled;
        disabled = value;

        triggerDisabledChange(source != null ? source : this, oldValue, true, bypassChecks);
    }

    private void triggerDisabledChange(BindableMap<K, V> source, boolean old, boolean propagate, boolean bypassChecks) {
        if (propagate)
            propagateDisabledChanged(source, disabled);

        if (old != disabled)
            disabledChanged.execute(new ValueChangedEvent<>(old, disabled));
    }

    private void propagateDisabledChanged(BindableMap<K, V> source, boolean value) {
        for (WeakReference<BindableMap<K, V>> binding : bindings) {
            if (binding.refersTo(source)) continue;

            BindableMap<K, V> bindable = binding.get();

            if (bindable == null) {
                bindings.remove(binding);
                continue;
            }

            bindable.setDisabled(value);
        }
    }

    @Override
    public boolean isDisabled() {
        return disabled;
    }

    @Override
    public void setDisabled(boolean disabled) {
        if (disabled == this.disabled) return;

        setDisabled(disabled, false, null);
    }

    @Override
    public void onDisabledChanged(ValuedAction<Boolean> action, boolean runOnceImmediately) {
        disabledChanged.add(action);

        if (runOnceImmediately)
            action.invoke(new ValueChangedEvent<>(disabled, disabled));
    }

    @Override
    public BindableMap<K, V> copy() {
        BindableMap<K, V> copy = createInstance();

        copy.bindTo(this);

        return copy;
    }

    @Override
    public BindableMap<K, V> copyTo(IBindableMap<K, V> other) {
        if (!(other instanceof BindableMap<K, V> copy)) return null;

        copy.map.clear();
        copy.map.putAll(map);

        copy.setDisabled(disabled, true, null);

        return copy;
    }

    @Override
    public BindableMap<K, V> getBoundCopy() {
        return (BindableMap<K, V>) IBindableMap.create(this);
    }

    @Override
    public BindableMap<K, V> getUnboundCopy() {
        return copy();
    }

    @Override
    public BindableMap<K, V> getWeakCopy() {
        return (BindableMap<K, V>) IBindableMap.createWeak(this);
    }

    @Override
    public BindableMap<K, V> bindTo(IBindableMap<K, V> other) {
        if (!(other instanceof BindableMap<K, V> bindable)) return null;

        if (bindings.contains(bindable.weakReference))
            throw new IllegalArgumentException(String.format("Attempted to bind %s to %s, but it was already bound", this.getClass().getSimpleName(), other.getClass().getSimpleName()));

        BindableMap<K, V> source = bindable.getBoundCopy();

        source.copyTo(this);

        refer(bindable);
        source.refer(this);

        return source;
    }

    @Override
    public BindableMap<K, V> weakBind(IBindableMap<K, V> other) {
        if (!(other instanceof BindableMap<K, V> bindable)) return null;

        if (bindings.contains(bindable.weakReference))
            throw new IllegalArgumentException(String.format("Attempted to bind %s to %s, but it was already bound", this.getClass().getSimpleName(), other.getClass().getSimpleName()));

        BindableMap<K, V> source = bindable.getWeakCopy();

        source.copyTo(this);
        source.refer(this);

        return source;
    }

    private void refer(BindableMap<K, V> bindable) {
        WeakReference<BindableMap<K, V>> reference = bindable.weakReference;

        if (bindings.contains(reference))
            throw new IllegalArgumentException(String.format("Attempted to add a binding to %s from %s, but it was already bound", this.getClass().getSimpleName(), bindable.getClass().getSimpleName()));

        bindings.add(reference);
    }

    private void unrefer(BindableMap<K, V> bindable) {
        WeakReference<BindableMap<K, V>> reference = bindable.weakReference;

        if (!bindings.contains(reference))
            throw new IllegalArgumentException(String.format("Attempted to remove a binding to %s from %s, but it was not bound", this.getClass().getSimpleName(), bindable.getClass().getSimpleName()));

        bindings.remove(reference);
    }

    @Override
    public void unbindEvents() {
        collectionChanged.clear();
        disabledChanged.clear();
    }

    @Override
    public void unbindWeak() {
        for (WeakReference<BindableMap<K, V>> binding : new ArrayList<>(bindings)) {
            final BindableMap<K, V> bindable = binding.get();

            if (bindable == null) {
                bindings.remove(binding);
                continue;
            }

            unbindWeakFrom(bindable);
        }

        bindings.clear();
    }

    @Override
    public void unbindBindings() {
        for (WeakReference<BindableMap<K, V>> binding : new ArrayList<>(bindings)) {
            final BindableMap<K, V> bindable = binding.get();

            if (bindable == null) {
                bindings.remove(binding);
                continue;
            }

            unbindFrom(bindable);
        }
    }

    @Override
    public void unbind() {
        unbindBindings();
        unbindWeak();
        unbindEvents();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void unbindFrom(IUnbindable other) {
        if (!(other instanceof BindableMap)) return;

        final BindableMap<K, V> bindable = (BindableMap<K, V>) other;

        unrefer(bindable);
        bindable.unrefer(this);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void unbindWeakFrom(IUnbindable other) {
        if (!(other instanceof BindableMap)) return;

        final BindableMap<K, V> bindable = (BindableMap<K, V>) other;

        bindable.unrefer(this);
    }

    @Override
    public String toString() {
        return "BindableMap{" +
                "map=" + map +
                ", disabled=" + disabled +
                '}';
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return map.containsValue(value);
    }

    @Override
    public V get(Object key) {
        return map.get(key);
    }

    @Override
    public V put(K key, V value) {
        return put(key, value, new HashSet<>());
    }

    protected V put(K key, V value, Set<BindableMap<K, V>> appliedInstances) {
        if (checkAlreadyApplied(appliedInstances))
            return null;

        ensureMutationAllowed();

        V oldValue = map.put(key, value);

        MapEvent.Type type = oldValue == null ? MapEvent.Type.ADD : MapEvent.Type.REPLACE;

        for (WeakReference<BindableMap<K, V>> binding : bindings) {
            final BindableMap<K, V> bindable = binding.get();

            if (bindable == null) {
                bindings.remove(binding);
                continue;
            }

            bindable.put(key, value, appliedInstances);
        }

        collectionChanged.execute(
                new MapEvent<>(
                        type,
                        getElements(key, value),
                        getElements(key, oldValue)
                )
        );

        return oldValue;
    }



    @Override
    public V remove(Object key) {
        return remove(key, new HashSet<>());
    }

    @SuppressWarnings("unchecked")
    protected V remove(Object key, Set<BindableMap<K, V>> appliedInstances) {
        if (checkAlreadyApplied(appliedInstances))
            return null;

        ensureMutationAllowed();

        V oldValue = map.remove(key);

        for (WeakReference<BindableMap<K, V>> binding : bindings) {
            final BindableMap<K, V> bindable = binding.get();

            if (bindable == null) {
                bindings.remove(binding);
                continue;
            }

            bindable.remove(key, appliedInstances);
        }

        collectionChanged.execute(
                new MapEvent<>(
                        MapEvent.Type.REMOVE,
                        Collections.emptyList(),
                        getElements((K) key, oldValue)
                )
        );

        return oldValue;
    }

    @Override
    public void putAll( Map<? extends K, ? extends V> m) {
        putAll(m, new HashSet<>());
    }

    @SuppressWarnings("unchecked")
    protected void putAll(Map<? extends K, ? extends V> m, Set<BindableMap<K, V>> appliedInstances) {
        if (checkAlreadyApplied(appliedInstances))
            return;

        ensureMutationAllowed();

        map.putAll(m);

        for (WeakReference<BindableMap<K, V>> binding : bindings) {
            final BindableMap<K, V> bindable = binding.get();

            if (bindable == null) {
                bindings.remove(binding);
                continue;
            }

            bindable.putAll(m, appliedInstances);
        }

        collectionChanged.execute(
                new MapEvent<>(
                        MapEvent.Type.ADD,
                        getElements((Map<K, V>) m),
                        Collections.emptyList()
                )
        );
    }

    @Override
    public void clear() {
        clear(new HashSet<>());
    }

    protected void clear(Set<BindableMap<K, V>> appliedInstances) {
        if (checkAlreadyApplied(appliedInstances))
            return;

        ensureMutationAllowed();

        map.clear();

        for (WeakReference<BindableMap<K, V>> binding : bindings) {
            final BindableMap<K, V> bindable = binding.get();

            if (bindable == null) {
                bindings.remove(binding);
                continue;
            }

            bindable.clear(appliedInstances);
        }

        collectionChanged.execute(
                new MapEvent<>(
                        MapEvent.Type.REMOVE,
                        Collections.emptyList(),
                        getElements(map)
                )
        );
    }

    
    @Override
    public Set<K> keySet() {
        return map.keySet();
    }

    
    @Override
    public Collection<V> values() {
        return map.values();
    }

    
    @Override
    public Set<Entry<K, V>> entrySet() {
        return map.entrySet();
    }

    private Collection<IMapEvent.Element<K, V>> getElements(Map<K, V> elements) {
        Collection<IMapEvent.Element<K, V>> collection = new ArrayList<>();

        for (Map.Entry<K, V> entry : elements.entrySet()) {
            collection.add(new IMapEvent.Element<>(entry.getKey(), entry.getValue()));
        }

        return collection;
    }
    private Collection<IMapEvent.Element<K, V>> getElements(K key, V value) {
        Collection<IMapEvent.Element<K, V>> collection = new ArrayList<>();

        collection.add(new IMapEvent.Element<>(key, value));

        return collection;
    }
    public enum MapType {
        HASH, LINKED, IDENTITY, WEAK
    }
}
