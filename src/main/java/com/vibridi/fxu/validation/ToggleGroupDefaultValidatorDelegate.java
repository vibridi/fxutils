package com.vibridi.fxu.validation;

import javafx.scene.control.Control;
import javafx.scene.control.Toggle;

public class ToggleGroupDefaultValidatorDelegate implements IValidatorDelegate {

	@Override
	public boolean isValid(Control control) {
		if(control instanceof Toggle) {
			return ((Toggle)control).getToggleGroup().getSelectedToggle() != null;
		}
		return false;
	}

}
