package com.vibridi.fxu.input;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.swing.text.DateFormatter;

import javafx.scene.control.Control;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;

public class FXInput {

	public static void setDateFormat(DatePicker datePicker, final String format) {
		datePicker.setConverter(new StringConverter<LocalDate>() {
			DateTimeFormatter df = DateTimeFormatter.ofPattern(format);
			
			@Override
			public String toString(LocalDate date) {
				return date != null ? df.format(date) : "";
			}

			@Override
			public LocalDate fromString(String string) {
				return (string != null && !string.isEmpty()) ? LocalDate.parse(string, df) : null;
			}
			
		});
		
	}
	
	public static void setTextFormatter(TextField textField) {
		
		textField.setTextFormatter(null);
		
		Control c = new TextField();
		
		DatePicker dp = new DatePicker();
		//dp.setform
		
	}

}
