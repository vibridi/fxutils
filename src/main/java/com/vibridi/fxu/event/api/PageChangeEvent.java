package com.vibridi.fxu.event.api;

import javafx.event.Event;
import javafx.event.EventType;

public class PageChangeEvent extends Event {
	private static final long serialVersionUID = 1L;
	
	public static final EventType<PageChangeEvent> PAGE_CHANGE = 
			new EventType<PageChangeEvent>(Event.ANY, "PAGE_CHANGE");

	public PageChangeEvent() {
		super(PAGE_CHANGE);
	}
}
