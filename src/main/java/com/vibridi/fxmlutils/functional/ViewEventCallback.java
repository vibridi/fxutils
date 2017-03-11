package com.vibridi.fxmlutils.functional;

@FunctionalInterface
public interface ViewEventCallback<T> {
	public void processEvent(T event);
}
