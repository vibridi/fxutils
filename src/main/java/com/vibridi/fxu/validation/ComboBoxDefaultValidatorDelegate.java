package com.vibridi.fxu.validation;

import javafx.scene.control.ComboBoxBase;
import javafx.scene.control.Control;

public class ComboBoxDefaultValidatorDelegate implements IValidatorDelegate {

	@Override
	public boolean isValid(Control control) {
		if(control instanceof ComboBoxBase<?>) {
			Object val = ((ComboBoxBase<?>)control).getValue();
			if(val instanceof String)
				return !val.equals("");
			else
				return val != null && !val.toString().equals("");
		}
		return false;
	}

}
