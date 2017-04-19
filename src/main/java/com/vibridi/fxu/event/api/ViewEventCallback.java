package com.vibridi.fxu.event.api;

@FunctionalInterface
public interface ViewEventCallback<T> {
	public void processEvent(T event);
}
