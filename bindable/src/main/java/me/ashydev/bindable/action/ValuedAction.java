package me.ashydev.bindable.action;

import me.ashydev.bindable.event.ValueChangedEvent;

public interface ValuedAction<T> extends Action<ValueChangedEvent<T>> { }
