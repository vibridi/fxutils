package com.vibridi.fxmlutils.api;

import com.vibridi.fxmlutils.functional.ViewEventCallback;

import javafx.stage.Modality;
import javafx.stage.Stage;

public interface IFXMLBuilder1 {
	public IFXMLBuilder1 addCallback(Class<?> triggerClazz, ViewEventCallback listener);
	public IFXMLBuilder1 setModality(Modality modality);
	public Stage build();
}
