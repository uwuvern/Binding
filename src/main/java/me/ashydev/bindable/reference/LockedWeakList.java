/*
 * Copyright (c) 2024 Ashley (uwuvern) <uwuvern@outlook.com>
 *
 * This project is licensed under the MIT license, check the root of the project for
 * more information.
 */

package me.ashydev.bindable.reference;

import java.lang.ref.WeakReference;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

public class LockedWeakList<T> extends ArrayList<WeakReference<T>> {
    public LockedWeakList(int initialCapacity) {
        super(initialCapacity);
    }

    public LockedWeakList() {

    }

    public LockedWeakList(Collection<? extends WeakReference<T>> c) {
        super(c);
    }

    @Override
    public synchronized void trimToSize() {
        super.trimToSize();
    }

    @Override
    public synchronized void ensureCapacity(int minCapacity) {
        super.ensureCapacity(minCapacity);
    }

    @Override
    public synchronized int size() {
        return super.size();
    }

    @Override
    public synchronized boolean isEmpty() {
        return super.isEmpty();
    }

    @Override
    public synchronized boolean contains(Object o) {
        return super.contains(o);
    }

    @Override
    public synchronized int indexOf(Object o) {
        return super.indexOf(o);
    }

    @Override
    public synchronized int lastIndexOf(Object o) {
        return super.lastIndexOf(o);
    }

    @Override
    public synchronized Object clone() {
        return super.clone();
    }

    @Override
    public synchronized Object[] toArray() {
        return super.toArray();
    }

    @Override
    public synchronized <T> T[] toArray(T[] a) {
        return super.toArray(a);
    }

    @Override
    public synchronized WeakReference<T> get(int index) {
        return super.get(index);
    }

    @Override
    public synchronized WeakReference<T> getFirst() {
        return super.getFirst();
    }

    @Override
    public synchronized WeakReference<T> getLast() {
        return super.getLast();
    }

    @Override
    public synchronized WeakReference<T> set(int index, WeakReference<T> element) {
        return super.set(index, element);
    }

    @Override
    public synchronized boolean add(WeakReference<T> tWeakReference) {
        return super.add(tWeakReference);
    }

    @Override
    public synchronized void add(int index, WeakReference<T> element) {
        super.add(index, element);
    }

    @Override
    public synchronized void addFirst(WeakReference<T> element) {
        super.addFirst(element);
    }

    @Override
    public synchronized void addLast(WeakReference<T> element) {
        super.addLast(element);
    }

    @Override
    public synchronized WeakReference<T> remove(int index) {
        return super.remove(index);
    }

    @Override
    public synchronized WeakReference<T> removeFirst() {
        return super.removeFirst();
    }

    @Override
    public synchronized WeakReference<T> removeLast() {
        return super.removeLast();
    }

    @Override
    public synchronized boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public synchronized int hashCode() {
        return super.hashCode();
    }

    @Override
    public synchronized boolean remove(Object o) {
        return super.remove(o);
    }

    @Override
    public synchronized void clear() {
        super.clear();
    }

    @Override
    public synchronized boolean addAll(Collection<? extends WeakReference<T>> c) {
        return super.addAll(c);
    }

    @Override
    public synchronized boolean addAll(int index, Collection<? extends WeakReference<T>> c) {
        return super.addAll(index, c);
    }

    @Override
    protected void removeRange(int fromIndex, int toIndex) {
        super.removeRange(fromIndex, toIndex);
    }

    @Override
    public synchronized boolean removeAll(Collection<?> c) {
        return super.removeAll(c);
    }

    @Override
    public synchronized boolean retainAll(Collection<?> c) {
        return super.retainAll(c);
    }

    @Override
    public synchronized ListIterator<WeakReference<T>> listIterator(int index) {
        return super.listIterator(index);
    }

    @Override
    public synchronized ListIterator<WeakReference<T>> listIterator() {
        return super.listIterator();
    }

    @Override
    public synchronized Iterator<WeakReference<T>> iterator() {
        return super.iterator();
    }

    @Override
    public synchronized List<WeakReference<T>> subList(int fromIndex, int toIndex) {
        return super.subList(fromIndex, toIndex);
    }

    @Override
    public synchronized void forEach(Consumer<? super WeakReference<T>> action) {
        super.forEach(action);
    }

    @Override
    public synchronized Spliterator<WeakReference<T>> spliterator() {
        return super.spliterator();
    }

    @Override
    public synchronized boolean removeIf(Predicate<? super WeakReference<T>> filter) {
        return super.removeIf(filter);
    }

    @Override
    public synchronized void replaceAll(UnaryOperator<WeakReference<T>> operator) {
        super.replaceAll(operator);
    }

    @Override
    public synchronized void sort(Comparator<? super WeakReference<T>> c) {
        super.sort(c);
    }

    @Override
    public synchronized boolean containsAll(Collection<?> c) {
        return super.containsAll(c);
    }

    @Override
    public synchronized String toString() {
        return super.toString();
    }

    @Override
    public synchronized List<WeakReference<T>> reversed() {
        return super.reversed();
    }

    @Override
    public synchronized <T> T[] toArray(IntFunction<T[]> generator) {
        return super.toArray(generator);
    }

    @Override
    public synchronized Stream<WeakReference<T>> stream() {
        return super.stream();
    }

    @Override
    public synchronized Stream<WeakReference<T>> parallelStream() {
        return super.parallelStream();
    }
}
