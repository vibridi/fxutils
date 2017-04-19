package com.vibridi.fxu.event;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class FXEvent {
	
	/**
	 * Convenience setter for double-click events. The handler that you pass to this function doesn't have to 
	 * explicitly check that the event is a double click. 
	 * 
	 * @param node
	 * @param doubleClickHandler
	 */
	public static void setOnDoubleClick(Node node, EventHandler<? super MouseEvent> doubleClickHandler) {
		node.setOnMouseClicked(event -> {
			if(event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
				doubleClickHandler.handle(event);
			}
		});
	}
}
