package com.vibridi.fxmlutils;

import java.io.IOException;
import java.net.URL;

import com.vibridi.fxmlutils.api.IFXMLBuilder;
import com.vibridi.fxmlutils.api.IFXMLBuilder1;
import com.vibridi.fxmlutils.controller.BaseController;
import com.vibridi.fxmlutils.exception.FXMLException;
import com.vibridi.fxmlutils.functional.ViewEventCallback;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class FXMLBuilder implements IFXMLBuilder {
	
	private FXMLLoader loader;
	private Stage stage;
	
	protected FXMLBuilder(URL url) {
		loader = new FXMLLoader(url);
	}

	@Override
	public IFXMLBuilder1 makeStage(String title) {
		try {	
			stage = new Stage();
			stage.setTitle(title);
			stage.setScene(new Scene(loader.load()));
			stage.setUserData(loader);
			return new FXMLBuilder1();
		} catch(IOException e) {
			throw new FXMLException("Can't load container from FXML: " + e.getMessage(), e);
		}
	}
	
	public class FXMLBuilder1 implements IFXMLBuilder1 {
		
		@Override
		public IFXMLBuilder1 addCallback(Class<?> triggerClazz, ViewEventCallback listener) {
			Object ctrl = loader.getController();
			if(ctrl instanceof BaseController)
				((BaseController) ctrl).addCallback(triggerClazz, listener);
			return this;
		}
		
		@Override
		public IFXMLBuilder1 setModality(Modality modality) {
			stage.initModality(modality);
			return this;
		}
		
		@Override
		public Stage build() {
			return stage;
		}
		
	}
	
}
