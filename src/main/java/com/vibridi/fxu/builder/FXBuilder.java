package com.vibridi.fxu.builder;

import java.io.IOException;
import java.net.URL;

import com.vibridi.fxu.builder.api.IFXBuilder;
import com.vibridi.fxu.builder.api.IFXBuilder1;
import com.vibridi.fxu.builder.api.IFXBuilder1_1;
import com.vibridi.fxu.builder.api.IFXBuilderFinalStage;
import com.vibridi.fxu.controller.BaseController;
import com.vibridi.fxu.event.api.ViewEventCallback;
import com.vibridi.fxu.exception.FXException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Provides a build pipeline for JavaFX views. The methods are chainable and enforce a basic call order.
 * It assumes that your .fxml resources are found in the class path of your application.
 * 
 * Example usage:
 * 
 * <pre>
 * FXBuilder.newView(myClazz, "view/MainView.fxml")
 * 		.makeStage("New Stage")
 * 		.setupWith(param1, param2)
 * 		.addCallback(String.class, this::stringCallback)
 *	 	.addCallback(MyTriggerClass.class, this::myCallback)
 * 		.setModality(Modality.WINDOW_MODAL)
 * 		.build()
 * 		.show();
 * </pre>
 *
 */
public class FXBuilder implements IFXBuilder {
	
	/**
	 * 	 
	 * @param clazz Class relative to which the <code>.fxml</code> file is found
	 * @param fxml Location of the <code>.fxml</code> file
	 * @return Instance of <code>FXBuilder</code>
	 */
	public static FXBuilder newView(Class<?> clazz, String fxml) {		
		return newView(clazz.getResource(fxml));
	}
	
	public static FXBuilder newView(URL url) {
		return new FXBuilder(url);
	}
	
	public static void justShow(Node n) {
		BorderPane bp = new BorderPane();
		bp.setCenter(n);
		justShow(bp);
	}
	
	public static void justShow(Node n, Double topAnchor, Double rightAnchor, Double bottomAnchor, Double leftAnchor) {
		BorderPane bp = new BorderPane();
		AnchorPane ap = new AnchorPane();
		bp.setCenter(ap);
		AnchorPane.setTopAnchor(n, topAnchor);
		AnchorPane.setRightAnchor(n, rightAnchor);
		AnchorPane.setBottomAnchor(n, bottomAnchor);
		AnchorPane.setLeftAnchor(n, leftAnchor);
		ap.getChildren().add(n);
		justShow(bp);
	}
	
	private static void justShow(Pane p) {
		p.setPrefSize(300, 150);
		Stage stage = new Stage();
		stage.setTitle("Just showing");
		stage.setScene(new Scene(p));
		stage.show();
	}
	
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
