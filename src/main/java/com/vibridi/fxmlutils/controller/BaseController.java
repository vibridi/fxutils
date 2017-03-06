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
	
	private MultiMap listeners;
		
	public BaseController() {
		listeners = MultiValueMap.decorate(new HashMap<String,ViewEventCallback>());
	}
	
	/**
	 * 
	 * @param listenerTriggerClazz Class of the object that will be passed to <code>ViewEventCallback.processEvent</code>
	 * @param listener Callback object or lambda expression
	 */
	public void addCallback(Class<?> listenerTriggerClazz, ViewEventCallback listener) {
		listeners.put(listenerTriggerClazz.getName(), listener);
	}
	
	protected void setTooltip(Node fxnode, String tip) {
		Tooltip.install(fxnode, new Tooltip(tip));
	}
	
	@SuppressWarnings("unchecked")
	protected void fireEvent(Object payload) {
		for(ViewEventCallback lsn : (List<ViewEventCallback>) listeners.get(payload.getClass().getName()))
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

