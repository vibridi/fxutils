package com.vibridi.fxmlutils.functional;

@FunctionalInterface
public interface ViewEventCallback {
	public void processEvent(Object eventPayload);
}
