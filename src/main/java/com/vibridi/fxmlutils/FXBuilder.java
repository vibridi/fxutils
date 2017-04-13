package com.vibridi.fxmlutils;

import java.io.IOException;
import java.net.URL;

import com.vibridi.fxmlutils.api.IFXBuilder;
import com.vibridi.fxmlutils.api.IFXBuilder1;
import com.vibridi.fxmlutils.api.IFXBuilder1_1;
import com.vibridi.fxmlutils.api.IFXBuilderFinalStage;
import com.vibridi.fxmlutils.controller.BaseController;
import com.vibridi.fxmlutils.exception.FXException;
import com.vibridi.fxmlutils.functional.ViewEventCallback;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class FXBuilder implements IFXBuilder {
	
	private FXMLLoader loader;
	
	protected FXBuilder(URL url) {
		loader = new FXMLLoader(url);
	}

	@Override
	public FXBuilder1 makeStage(String title) {
		try {	
			Stage stage = new Stage();
			stage.setTitle(title);
			stage.setScene(new Scene(loader.load()));
			stage.setUserData(loader);
			return new FXBuilder1(stage);
		} catch(IOException e) {
			throw new FXException("Can't load container from FXML: " + e.getMessage(), e);
		}
	}
	
	public <T extends Pane> FXBuilder1_1<T> makePane(Class<T> clazz) {
		try {
			return new FXBuilder1_1<T>(loader.load());
		} catch(IOException e) {
			throw new FXException("Can't load container from FXML: " + e.getMessage(), e);
		}
	}
	
	public class FXBuilder1 implements IFXBuilder1<FXBuilder1>, IFXBuilderFinalStage<Stage> {
		
		private Stage stage;
		
		protected FXBuilder1(Stage stage) {
			this.stage = stage;	
			Object ctrl = loader.getController();
			if(ctrl instanceof BaseController) {
				((BaseController) ctrl).setStage(this.stage);
				((BaseController) ctrl).setup();
			}
		}
		
		@Override
		public FXBuilder1 setupWith(Object... parameters) {
			Object ctrl = loader.getController();
			if(ctrl instanceof BaseController) {
				((BaseController) ctrl).setupWith(parameters);
			}
			return this;
		}
		
		@Override
		public <T> FXBuilder1 addCallback(Class<T> triggerClazz, ViewEventCallback<T> listener) {
			addCallbackToCtrl(triggerClazz, listener);
			return this;
		}
		
		@Override
		public FXBuilder1 setModality(Modality modality) {
			stage.initModality(modality);
			return this;
		}
		
		@Override
		public Stage build() {
			return stage;
		}
	}
	
	public class FXBuilder1_1<T extends Pane> implements IFXBuilder1_1<FXBuilder1_1<T>>, IFXBuilderFinalStage<T> {

		private T pane;
		
		protected FXBuilder1_1(T pane) {
			this.pane = pane;
		}
		
		@Override
		public <U> FXBuilder1_1<T> addCallback(Class<U> triggerClazz, ViewEventCallback<U> listener) {
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
