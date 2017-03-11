package com.vibridi.fxmlutils.controller;

import java.util.HashMap;
import java.util.List;

import org.apache.commons.collections.MultiMap;
import org.apache.commons.collections.map.MultiValueMap;

import com.vibridi.fxmlutils.functional.ViewEventCallback;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Tooltip;
import javafx.stage.Stage;

public abstract class BaseController {
	
	protected Stage stage;
	private MultiMap listeners;
		
	public <T> BaseController() {
		listeners = MultiValueMap.decorate(new HashMap<String,ViewEventCallback<T>>());
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
	}
	
	protected void setTooltip(Node fxnode, String tip) {
		Tooltip.install(fxnode, new Tooltip(tip));
	}
	
	@SuppressWarnings("unchecked")
	protected <T> void fireEvent(T payload) {
		for(ViewEventCallback<T> lsn : (List<ViewEventCallback<T>>) listeners.get(payload.getClass().getName()))
			lsn.processEvent(payload);
	}
	
	/**
	 * Closes the stage associated with this controller. 
	 * 
	 * The method accepts an event parameter for two reasons: <br> 
	 * <ul>
	 * <li>the <code>Stage</code> can be traced and retrieved from an <code>ActionEvent</code></li>
	 * <li>usability: disappearance of a UI element must be a behavior that the user can reasonably expect</li>
	 * </ul> 
	 * 
	 * @param event The event that triggers the window closing
	 */
	protected void close(ActionEvent event) {
		Node source = (Node) event.getSource();
		Stage stage = (Stage) source.getScene().getWindow();
		stage.close();
	}
	
}

