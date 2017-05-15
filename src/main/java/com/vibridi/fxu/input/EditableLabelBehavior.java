package com.vibridi.fxu.input;

import com.sun.javafx.scene.control.behavior.TextFieldBehavior;
import javafx.css.PseudoClass;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

/**
 * The Behavior Class for EditableLabel
 *
 */
@SuppressWarnings("restriction")
public class EditableLabelBehavior extends TextFieldBehavior {

	private EditableLabel editableLabel;
	private Boolean focusTraversable;

	public EditableLabelBehavior(final EditableLabel editableLabel) {
		super(editableLabel);
		this.editableLabel = editableLabel;
		init();
	}

	private void init() {
		focusTraversable = false;

		// Register listeners and events
		editableLabel.setOnMouseClicked(this::handleMouseClicked);
		editableLabel.setOnKeyPressed(this::handleKeyPressed);
		editableLabel.focusedProperty().addListener((observable, oldValue, newValue) -> handleFocusChange(newValue));
		editableLabel.focusTraversableProperty().addListener((observable, oldValue, newValue) -> handleFocusTraversableChange(newValue));
	}

	/************************************************************************
	 * @}                                                                   *
	 *                                                                      *
	 * \defgroup BehaviorMethods                                            *
	 *                                                                      *
	 * @{                                                                   *
	 ***********************************************************************/

	private void handleKeyPressed(KeyEvent event) {
		switch (event.getCode()) {
		case ENTER:
			editableLabel.setBaseText(editableLabel.getText());
			exitEditableMode();
			break;
		case ESCAPE:
			exitEditableMode();
			break;
		default:	// dummy default to suppress 'incomplete-switch' warning
			break;
		}
	}

	private void handleMouseClicked(MouseEvent event) {
		if (event.getClickCount() == editableLabel.getEditableClicks() && !this.isEditing()) {
			enterEditableMode();
		}
	}

	private void handleFocusChange(Boolean newValue) {
		if ( !newValue ) {
			// Save changes and exit editable mode
			editableLabel.setBaseText(editableLabel.getText());
			exitEditableMode();
		} else if ( focusTraversable ){
			enterEditableMode();
		}
	}

	private void handleFocusTraversableChange(Boolean newValue) {
		focusTraversable = newValue;
	}

	private void enterEditableMode() {
		editableLabel.setEditable(true);
		editableLabel.deselect();
		editableLabel.pseudoClassStateChanged(PseudoClass.getPseudoClass("editable"), true);
	}

	private void exitEditableMode() {
		editableLabel.setEditable(false);
		editableLabel.deselect();
		editableLabel.pseudoClassStateChanged(PseudoClass.getPseudoClass("editable"), false);
	}

}