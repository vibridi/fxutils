package com.vibridi.fxu.input;

import javafx.scene.control.DatePicker;

public class FormattableDatePicker extends DatePicker {

	public FormattableDatePicker(String dateFormat) {
		super();
		FXInput.setDateFormat(this, dateFormat);
		
		this.setOnAction(event -> {
			System.out.println(this.getValue());
		});
	}

}
