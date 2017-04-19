package com.vibridi.fxu.builder.api;

import com.vibridi.fxu.event.api.ViewEventCallback;

import javafx.stage.Modality;
import javafx.stage.Stage;

public interface IFXBuilder1Callback<R> {
	public <T> R addCallback(Class<T> triggerClazz, ViewEventCallback<T> listener);
}
