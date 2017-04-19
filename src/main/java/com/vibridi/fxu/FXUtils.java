package com.vibridi.fxu;

import javafx.scene.Node;
import javafx.scene.control.Tooltip;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.StackPane;

public class FXUtils {

	public static void copyToClipboard(String text) {
		ClipboardContent content = new ClipboardContent();
		content.putString(text);
		Clipboard.getSystemClipboard().setContent(content);
	}
	
	public static void turnPage(StackPane stack) {
		stack.getChildren().get(stack.getChildren().size() - 1).toBack();
	}
	
	/**
	 * Sets a mouse-over tooltip on the specified node.
	 * 
	 * @param node Node that has to show the tip
	 * @param text Informative content of the tip
	 */
	public static void setTooltip(Node node, String text) {
		Tooltip.install(node, new Tooltip(text));
	}
}
