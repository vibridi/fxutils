package com.vibridi.fxu.validation;

import javafx.scene.control.Control;
import javafx.scene.control.TextField;

public class TextFieldDefaultValidatorDelegate implements IValidatorDelegate {

	@Override
	public boolean isValid(Control control) {
		if(control instanceof TextField) {
			return !(((TextField)control).getText().equals(""));
		}
		return false;
	}

}
