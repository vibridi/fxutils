package com.vibridi.fxu.input;

import java.util.List;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.input.KeyEvent;

public class AutoComplete {
	
	public interface AutoCompleteComparator<T> {
		public boolean matches(String typedText, T objectToCompare);
	}

	public static <T> void setAutoComplete(ComboBox<T> comboBox, AutoCompleteComparator<T> comparatorMethod) {
		ObservableList<T> data = comboBox.getItems();
		int r = comboBox.getVisibleRowCount();
		
		comboBox.setEditable(true);
		comboBox.addEventHandler(KeyEvent.KEY_PRESSED, e -> comboBox.hide());
		comboBox.addEventHandler(KeyEvent.KEY_RELEASED, (KeyEvent event) -> {
			
			switch(event.getCode()) {
			
			case RIGHT:
			case LEFT:
			case SHIFT:
			case HOME:
			case END:
			case TAB:
				return;
			
			case BACK_SPACE:
			case DELETE:
				break;
				
			default:
				if(!event.getCode().isLetterKey())
					return;
			}
			
			
			if(comboBox.getEditor().getText() == null || comboBox.getEditor().getText().length() < 3) {
				comboBox.setItems(data);
				comboBox.setVisibleRowCount(r);
				comboBox.show();
				return;
			}
		
			List<T> list = comboBox.getItems().stream()
					.filter(obj -> comparatorMethod.matches(comboBox.getEditor().getText(), obj))
					.collect(Collectors.toList());
			
			comboBox.setItems(FXCollections.observableArrayList(list));
			
			if(!list.isEmpty()) {
				comboBox.setVisibleRowCount(Math.min(r, comboBox.getItems().size()));
				comboBox.show();
			}
		});
	}
}
