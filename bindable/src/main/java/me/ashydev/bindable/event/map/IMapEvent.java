package me.ashydev.bindable.event.map;

import java.util.Collection;

public interface IMapEvent<K, E> {
    Collection<Element<K, E>> getNew();
    Collection<Element<K, E>> getOld();

    Type getType();

    enum Type {
        ADD, REMOVE, REPLACE
    }

    class Element<K, E> {
        private final K key;
        private final E element;

        public Element(K key, E element) {
            this.key = key;
            this.element = element;
        }

        public K getKey() {
            return key;
        }

        public E getElement() {
            return element;
        }

        @Override
        public String toString() {
            return "Element{" +
                    "key=" + key +
                    ", element=" + element +
                    '}';
        }
    }
}
