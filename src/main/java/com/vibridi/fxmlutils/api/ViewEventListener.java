package com.vibridi.fxmlutils.api;

@FunctionalInterface
public interface ViewEventListener {
	public void processEvent(Object eventPayload);
}
