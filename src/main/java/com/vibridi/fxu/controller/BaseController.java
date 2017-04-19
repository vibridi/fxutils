package com.vibridi.fxu.controller;

import java.util.HashMap;
import java.util.List;

import org.apache.commons.collections.MultiMap;
import org.apache.commons.collections.map.MultiValueMap;

import com.vibridi.fxu.event.api.ViewEventCallback;

import javafx.scene.Node;
import javafx.scene.control.Tooltip;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public abstract class BaseController {
	
	protected Stage stage;
	private MultiMap listeners;
		
	public <T> BaseController() {
		listeners = MultiValueMap.decorate(new HashMap<String,ViewEventCallback<T>>());
	}
	
	/**
	 * This method is always called after <code>initialize()</code> to manage additional post-initialization logic.
	 * The stage object is available here. If the implementor does not override this, then controller initialization is
	 * limited to the <code>initialize()</code> called by the JavaFX framework. 
	 * 
	 */
	public void setup() {
		// do nothing by default 
	}
	
	/**
	 * Same as {@link BaseController#setup()} but allowing multiple arbitrary parameters.
	 * The method in this base class makes no assumptions about parameter types. Implementor and caller must work in 
	 * coordination to ensure type safety.
	 * This method must be called explicitly by whomever instantiates the view.
	 * 
	 * @param parameters
	 */
	public void setupWith(Object... parameters) {
		// do nothing by default 
	}
	
	/**
	 * 
	 * @param listenerTriggerClazz Class of the object that will be passed to <code>ViewEventCallback.processEvent</code>
	 * @param listener Callback object or lambda expression
	 */
	public <T> void addCallback(Class<T> listenerTriggerClazz, ViewEventCallback<T> listener) {
		listeners.put(listenerTriggerClazz.getName(), listener);
	}
	
	public void setStage(Stage stage) {
		this.stage = stage;
		this.stage.setOnCloseRequest(this::handleCloseRequest);
	}
	
	protected void setTooltip(Node fxnode, String tip) {
		Tooltip.install(fxnode, new Tooltip(tip));
	}
	
	@SuppressWarnings("unchecked")
	protected <T> void fireEvent(T payload) {
		for(ViewEventCallback<T> lsn : (List<ViewEventCallback<T>>) listeners.get(payload.getClass().getName()))
			lsn.processEvent(payload);
	}
	
	protected abstract void handleCloseRequest(WindowEvent event);
	
}

