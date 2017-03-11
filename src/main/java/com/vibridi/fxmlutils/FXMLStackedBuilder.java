package com.vibridi.fxmlutils;

import java.io.IOException;
import java.net.URL;

import com.vibridi.fxmlutils.api.IFXMLBuilder;
import com.vibridi.fxmlutils.api.IFXMLBuilder1;
import com.vibridi.fxmlutils.controller.BaseStackController;
import com.vibridi.fxmlutils.controller.BaseStackPageController;
import com.vibridi.fxmlutils.exception.FXMLException;
import com.vibridi.fxmlutils.functional.ViewEventCallback;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class FXMLStackedBuilder {

	private Stage stage;
	private Node node;
	private IFXMLBuilder builder;
	private IFXMLBuilder1 builder1;
	
	private final boolean isSimple;
	
	protected FXMLStackedBuilder(URL url) {
		builder = new FXMLBuilder(url);
		isSimple = false;
		
	}
	
	protected FXMLStackedBuilder() {
		isSimple = true;
	}
	
	/**
	 * Stage 0: builds a stacked pane view from its own .fxml descriptor 
	 */
	public FXMLStackedBuilder1Adapter makeStage(String title) {
		if(isSimple) {
			stage = new Stage();
			stage.setTitle(title);
			stage.setScene(new Scene(new BorderPane()));
			stage.setUserData(new BaseStackController() {
				
			});
			return new FXMLSimpleStackedBuilder1();
		} else {
			builder1 = builder.makeStage(title);
			return new FXMLStackedBuilder1();
		}
	}
	
	public interface FXMLStackedBuilder1Adapter {
		public FXMLStackedBuilder2 buildStack();
	}
	
	/**
	 * Stage 1: builds callbacks and modality onto the stack controller
	 */
	public class FXMLStackedBuilder1 implements FXMLStackedBuilder1Adapter {
		public FXMLStackedBuilder1 addCallback(Class<?> triggerClazz, ViewEventCallback callback) {
			builder1 = builder1.addCallback(triggerClazz, callback);
			return this;
		}
		
		public FXMLStackedBuilder1 setModality(Modality modality) {
			builder1 = builder1.setModality(modality);
			return this;
		}
		
		@Override
		public FXMLStackedBuilder2 buildStack() {
			stage = builder1.build();
			return new FXMLStackedBuilder2();
		}
	}
	
	public class FXMLSimpleStackedBuilder1 implements FXMLStackedBuilder1Adapter {

		@Override
		public FXMLStackedBuilder2 buildStack() {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
	
	/**
	 * Stage 2: builds child pages onto the stacked pane 
	 */
	public class FXMLStackedBuilder2 {
		
		public FXMLStackedBuilder3 makePage(String fxml, Class<?> clazz) {
			return makePage(clazz.getResource(fxml));
		}
		
		public FXMLStackedBuilder3 makePage(URL url) {
			try {
				FXMLLoader pageLoader = new FXMLLoader(url);
				node = pageLoader.load();
				Object childObj = pageLoader.getController();
				if(childObj instanceof BaseStackPageController) {
					BaseStackPageController childCtrl = (BaseStackPageController) childObj;
					Object parentObj = ((FXMLLoader) stage.getUserData()).getController();
					if(parentObj instanceof BaseStackController) {
						childCtrl.addStackPageCallback(() -> 
							((BaseStackController) parentObj).nextChild());
					}
				}
				return new FXMLStackedBuilder3();
			} catch(IOException e) {
				throw new FXMLException("Can't load stack page from FXML: " + e.getMessage(), e);
			}
		}
		
		public Stage build() {
			return stage;
		}
		
	}
	
	public class FXMLStackedBuilder3 {
		public FXMLStackedBuilder2 addPage() {
			Object ctrl = ((FXMLLoader) stage.getUserData()).getController();
			if(ctrl instanceof BaseStackController) {
				((BaseStackController) ctrl).addChild(node);
			}
			return new FXMLStackedBuilder2();
		}
	}

	

	
	
}
