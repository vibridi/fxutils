package com.vibridi.fxu.validation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.css.PseudoClass;
import javafx.scene.control.ComboBoxBase;
import javafx.scene.control.Control;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;

public class FormValidator {

	public static final PseudoClass STYLE_ERROR_CLASS = PseudoClass.getPseudoClass("error");
	
	private Map<Control, IValidatorDelegate> map;
	private List<Control> failed;
	
	public FormValidator() {
		map = new HashMap<>();
		failed = new ArrayList<>();
	}
	
	public void addControl(TextField textField) {
		map.put(textField, new TextFieldDefaultValidatorDelegate());
	}
	
	public void addControl(TextField textField, IValidatorDelegate delegate) {
		map.put(textField, delegate);
	}
	
	public void addControl(ComboBoxBase<?> combo) {
		map.put(combo, new ComboBoxDefaultValidatorDelegate());
	}
	
	public void addControl(ToggleGroup toggleGroup) {
		Toggle toggle = toggleGroup.getToggles().get(0);
		if(!(toggle instanceof Control))
			return;
		map.put((Control)toggle, new ToggleGroupDefaultValidatorDelegate());
	}
	
	public void addControl(Control control, IValidatorDelegate delegate) {
		map.put(control, delegate);
	}
	
	public boolean isValid() {
		cancelHighlight();
		boolean valid = true;
		for(Control c : map.keySet()) {
			if(!map.get(c).isValid(c)) {
				failed.add(c);
				valid = false;
			}
		}
		return valid;
	}
	
	public void highlightInvalid() {
		for(Control c : failed) {
			c.pseudoClassStateChanged(STYLE_ERROR_CLASS, true);
		}
	}
	
	public void cancelHighlight() {
		for(Control c : failed) {
			c.pseudoClassStateChanged(STYLE_ERROR_CLASS, false);
		}
		failed.clear();
	}
	
}
