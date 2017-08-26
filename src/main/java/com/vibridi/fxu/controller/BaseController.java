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

/**
 * Abstract class that provides some basic shared functionality for JavaFX controller classes, as:
 * <ul>
 * <li>code that works with the Stage object</li>
 * <li>code that works with initialization parameters</li>
 * <li>a simple infrastructure to manage events according to their type signature</li>
 * </ul>
 *
 */
public abstract class BaseController {
	
	protected Stage stage;
	private MultiMap listeners;
		
	public <T> BaseController() {
		listeners = MultiValueMap.decorate(new HashMap<String,ViewEventCallback<T>>());
	}
	
	/**
	 * This method is always called after <code>initialize()</code> to manage additional post-initialization logic.
	 * The stage object is available here. 
	 * If the implementor does not override this, then the controller initialization is limited to 
	 * the <code>initialize()</code> method called by the JavaFX framework. 
	 */
	public void setup() {
		// do nothing by default 
	}
	
	/**
	 * Same as {@link BaseController#setup()} but allows multiple arbitrary parameters.
	 * The method in this base class makes no assumptions about parameter types. Type checks are up to the user.
	 * This method must be called by whom instantiates the view.
	 * 
	 * @param parameters Setup parameters
	 */
	public void setupWith(Object... parameters) {
		// do nothing by default 
	}
	
	/**
	 * Adds a callback that triggers when {@link BaseController#fireEvent} is called with a certain type.
	 * @param listenerTriggerClazz Type of object that will be passed to {@link ViewEventCallback#processEvent}.
	 * @param listener Callback object or lambda expression
	 * @param <T> Type of object that will trigger this callback
	 */
	public <T> void addCallback(Class<T> listenerTriggerClazz, ViewEventCallback<T> listener) {
		listeners.put(listenerTriggerClazz.getName(), listener);
	}
	
	public void setStage(Stage stage) {
		this.stage = stage;
		this.stage.setOnCloseRequest(this::handleCloseRequest);
	}
	
	/**
	 * @deprecated Use FXUtils.setTooltip instead.
	 * @param fxnode Node that will show the tooltip when hovered
	 * @param tip Text of the tooltip
	 */
	protected void setTooltip(Node fxnode, String tip) {
		Tooltip.install(fxnode, new Tooltip(tip));
	}
	
	/**
	 * Fires an event. This method triggers all the callbacks that accept a paramater of the same type as this method.
	 * @param payload The object passed to the relevant callbacks.
	 * @param <T> Type of the object passed to the relevant callbacks.
	 */
	@SuppressWarnings("unchecked")
	protected <T> void fireEvent(T payload) {
		List<ViewEventCallback<T>> list = (List<ViewEventCallback<T>>) listeners.get(payload.getClass().getName());
		if(list == null)
			return;
		
		for(ViewEventCallback<T> lsn : list)
			lsn.processEvent(payload);
	}
	
	/**
	 * Closes the stage associated to this controller. It fires a <code>WINDOW_CLOSE_REQUEST</code>, so to allow FX to 
	 * properly pass the event down the object hierarchy. 
	 * If the event is not consumed at the upper levels, it will trigger the {@link BaseController#handleCloseRequest}
	 */
	protected void closeEvent() {
		stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
	}
	
	/**
	 * Closes the stage associated to this controller. Methods dependent on close events might not get called.
	 */
	protected void closeNow() {
		stage.close();
	}
	
	protected abstract void handleCloseRequest(WindowEvent event);
	
}

