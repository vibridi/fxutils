package com.vibridi.fxu.input;

import java.util.List;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.scene.control.ComboBox;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class AutoComplete {
	
	public interface AutoCompleteComparator<T> {
		public boolean matches(String typedText, T objectToCompare);
	}

	public static <T> void setAutoComplete(ComboBox<T> comboBox, AutoCompleteComparator<T> comparatorMethod) {
		comboBox.setEditable(true);
		comboBox.addEventHandler(KeyEvent.KEY_RELEASED, (KeyEvent event) -> {
			if (event.getCode() != KeyCode.ALPHANUMERIC) {
				return;
			}
			
			List<T> list = comboBox.getItems().stream()
					.filter(obj -> comparatorMethod.matches(comboBox.getEditor().getText(), obj))
					.collect(Collectors.toList());
			
			comboBox.setItems(FXCollections.observableArrayList(list));
		});
	}
	
	public static void setAutoComplete(ComboBox<String> comboBox) {
		comboBox.setEditable(true);
		comboBox.addEventHandler(KeyEvent.KEY_RELEASED, (KeyEvent event) -> {
			if (event.getCode() != KeyCode.ALPHANUMERIC) {
				return;
			}
			
			List<String> list = comboBox.getItems().stream()
					.filter(s -> s.contains(comboBox.getEditor().getText()))
					.collect(Collectors.toList());
			
			comboBox.setItems(FXCollections.observableArrayList(list));
		});
	}
}
