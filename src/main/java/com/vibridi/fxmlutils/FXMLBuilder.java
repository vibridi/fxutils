package com.vibridi.fxmlutils;

import java.io.IOException;
import java.net.URL;

import com.vibridi.fxmlutils.api.ViewEventListener;
import com.vibridi.fxmlutils.controller.BaseController;
import com.vibridi.fxmlutils.exception.FXMLException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class FXMLBuilder {
	
	FXMLLoader loader;
	Stage stage;
	
	protected FXMLBuilder(String fxml, Class<?> clazz) {
		this(clazz.getResource(fxml));
	}
	
	protected FXMLBuilder(URL url) {
		loader = new FXMLLoader(url);
	}

	public FXMLBuilder1 buildView(String title) {
		try {	
			stage = new Stage();
			stage.setTitle(title);
			Scene scene = new Scene(loader.load());
			stage.setScene(scene);
			return new FXMLBuilder1();
		} catch(IOException e) {
			e.printStackTrace();
			throw new FXMLException("Can't load container from FXML: " + e.getMessage());
		}
	}
	
	public class FXMLBuilder1 {
		public FXMLBuilder1 addListener(Class<?> triggerClazz, ViewEventListener listener) {
			Object ctrl = loader.getController();
			if(ctrl instanceof BaseController)
				((BaseController) ctrl).addListener(triggerClazz, listener);
			return this;
		}
		
		public FXMLBuilder1 setModality(Modality modality) {
			stage.initModality(modality);
			return this;
		}
		
	}
	
}
