package com.vibridi.fxmlutils.api;

import com.vibridi.fxmlutils.functional.ViewEventCallback;

import javafx.stage.Modality;
import javafx.stage.Stage;

public interface IFXBuilder1Callback<R> {
	public <T> R addCallback(Class<T> triggerClazz, ViewEventCallback<T> listener);
}
