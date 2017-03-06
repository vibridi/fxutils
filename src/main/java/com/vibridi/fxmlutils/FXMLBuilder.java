package com.vibridi.fxmlutils;

import java.io.IOException;
import java.net.URL;

import com.vibridi.fxmlutils.controller.BaseController;
import com.vibridi.fxmlutils.exception.FXMLException;
import com.vibridi.fxmlutils.functional.ViewEventCallback;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class FXMLBuilder {
	
	private FXMLLoader loader;
	private Stage stage;
	
	protected FXMLBuilder(URL url) {
		loader = new FXMLLoader(url);
	}

	public FXMLBuilder1 makeStage(String title) {
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
	
//	protected Node makeRoot() {
//		try {
//			return (Node) loader.load();
//		} catch (IOException e) {
//			throw new FXMLException("Can't load container from FXML: " + e.getMessage(), e);
//		}
//	}
	
	public class FXMLBuilder1 {
		public FXMLBuilder1 addCallback(Class<?> triggerClazz, ViewEventCallback listener) {
			Object ctrl = loader.getController();
			if(ctrl instanceof BaseController)
				((BaseController) ctrl).addCallback(triggerClazz, listener);
			return this;
		}
		
		public FXMLBuilder1 setModality(Modality modality) {
			stage.initModality(modality);
			return this;
		}
		
		public Stage build() {
			return stage;
		}
		
	}
	
}
