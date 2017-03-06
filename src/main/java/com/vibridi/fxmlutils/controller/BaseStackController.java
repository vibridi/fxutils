package com.vibridi.fxmlutils.controller;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;

public abstract class BaseStackController extends BaseController {
	
	@FXML protected StackPane stack;
	
	private boolean firstChild;
	
	public BaseStackController() {
		super();
		firstChild = true;
	}
	
	public void addChild(Node node) {
		stack.getChildren().add(node);
		if(firstChild) {
			node.toFront();
			firstChild = false;
		}
	}
	
	public void nextChild() {
		stack.getChildren().get(stack.getChildren().size() - 1).toBack();
	}
}
