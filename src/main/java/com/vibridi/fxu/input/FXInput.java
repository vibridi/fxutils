package com.vibridi.fxu.input;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javafx.scene.control.DatePicker;
import javafx.util.StringConverter;

/**
 * Utility methods for JavaFX input controls.
 *
 */
public class FXInput {

	/**
	 * Sets a custom date format on a {@link DatePicker}. The format must be compliant with the java.time specifications.
	 * @param datePicker The DatePicker object
	 * @param format The date format
	 */
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
	
//	public static void setTextFormatter(TextField textField) {
//		
//		textField.setTextFormatter(null);
//		
//		Control c = new TextField();
//		
//		DatePicker dp = new DatePicker();
//		//dp.setform
//		// TODO complete
//	}

}
