package com.vibridi.fxu.input;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.sun.javafx.scene.control.skin.DatePickerSkin;

import javafx.scene.control.DatePicker;
import javafx.scene.control.Skin;
import javafx.util.StringConverter;

public class FormattedDatePicker extends DatePicker {
	
	public static void decorate(DatePicker picker, String pattern) {
		picker.setPromptText(pattern);
		picker.setConverter(of(pattern));
	}
	
	public FormattedDatePicker(String pattern) {
		super();
		this.setPromptText(pattern);
		this.setConverter(of(pattern));
	}
	
	private static StringConverter<LocalDate> of(String pattern) {
		return new StringConverter<LocalDate>() {
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern(pattern);
			
			@Override
			public LocalDate fromString(String string) {
				if(string != null && !string.isEmpty())
					return LocalDate.parse(string, dtf);
				else
					return LocalDate.of(1900, 1, 1); // default date
			}

			@Override
			public String toString(LocalDate date) {
				return date != null ? dtf.format(date) : "";
			}
			
		};
	}
	
}
