package com.vibridi.fxmlutils;

import java.io.IOException;
import java.net.URL;

import com.vibridi.fxmlutils.api.IFXMLBuilder;
import com.vibridi.fxmlutils.api.IFXMLBuilder1;
import com.vibridi.fxmlutils.api.IFXMLBuilder1_1;
import com.vibridi.fxmlutils.api.IFXMLBuilderFinalStage;
import com.vibridi.fxmlutils.controller.BaseController;
import com.vibridi.fxmlutils.exception.FXMLException;
import com.vibridi.fxmlutils.functional.ViewEventCallback;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class FXMLBuilder implements IFXMLBuilder {
	
	private FXMLLoader loader;
	
	protected FXMLBuilder(URL url) {
		loader = new FXMLLoader(url);
	}

	@Override
	public FXMLBuilder1 makeStage(String title) {
		try {	
			Stage stage = new Stage();
			stage.setTitle(title);
			stage.setScene(new Scene(loader.load()));
			stage.setUserData(loader);
			return new FXMLBuilder1(stage);
		} catch(IOException e) {
			throw new FXMLException("Can't load container from FXML: " + e.getMessage(), e);
		}
	}
	
	public <T extends Pane> FXMLBuilder1_1<T> makePane(Class<T> clazz) {
		try {
			return new FXMLBuilder1_1<T>(loader.load());
		} catch(IOException e) {
			throw new FXMLException("Can't load container from FXML: " + e.getMessage(), e);
		}
	}
	
	public class FXMLBuilder1 implements IFXMLBuilder1<FXMLBuilder1>, IFXMLBuilderFinalStage<Stage> {
		
		private Stage stage;
		
		protected FXMLBuilder1(Stage stage) {
			Object ctrl = loader.getController();
			if(ctrl instanceof BaseController)
				((BaseController) ctrl).setStage(stage);
			this.stage = stage;
		}
		
		@Override
		public <T> FXMLBuilder1 addCallback(Class<T> triggerClazz, ViewEventCallback<T> listener) {
			addCallbackToCtrl(triggerClazz, listener);
			return this;
		}
		
		@Override
		public FXMLBuilder1 setModality(Modality modality) {
			stage.initModality(modality);
			return this;
		}
		
		@Override
		public Stage build() {
			return stage;
		}
	}
	
	public class FXMLBuilder1_1<T extends Pane> implements IFXMLBuilder1_1<FXMLBuilder1_1<T>>, IFXMLBuilderFinalStage<T> {

		private T pane;
		
		protected FXMLBuilder1_1(T pane) {
			this.pane = pane;
		}
		
		@Override
		public <U> FXMLBuilder1_1<T> addCallback(Class<U> triggerClazz, ViewEventCallback<U> listener) {
			addCallbackToCtrl(triggerClazz, listener);
			return this;
		}
		
		@Override
		public T build() {
			return pane;
		}
	}
	
	private <T> void addCallbackToCtrl(Class<T> triggerClazz, ViewEventCallback<T> listener) {
		Object ctrl = loader.getController();
		if(ctrl instanceof BaseController)
			((BaseController) ctrl).addCallback(triggerClazz, listener);
	}
	
}
