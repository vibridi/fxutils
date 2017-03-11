package com.vibridi.fxmlutils;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.vibridi.fxmlutils.FXMLStackedBuilder.FXMLStackedBuilder2;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class FXMLUtils {
	
	/**
	 * 
	 * @param fxml Path to the <code>.fxml</code> file
	 * @param clazz Class which the path of the <code>.fxml</code> file is relative to.
	 * @return Instance of <code>FXMLBuilder</code>
	 */
	public static FXMLBuilder newView(String fxml, Class<?> clazz) {
		return newView(clazz.getResource(fxml));
	}
	
	public static FXMLBuilder newView(URL url) {
		return new FXMLBuilder(url);
	}
	
	public static FXMLStackedBuilder newStackedView(String fxml, Class<?> clazz) {
		return newStackedView(clazz.getResource(fxml));
	}
	
	public static FXMLStackedBuilder newStackedView(URL url) {
		return new FXMLStackedBuilder(url);
	}
	
	public static void newSimpleStackedView() {
		new FXMLStackedBuilder().new FXMLStackedBuilder2();
	}
	
	public static Alert errorAlert(String message) {
		return errorAlert(null, message, null);
	}
	
	public static Alert errorAlert(String header, String message) {
		return errorAlert(header, message, null);
	}
	
	public static Alert errorAlert(String header, String message, Throwable t) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText(header);
		alert.setContentText(message);
		if(t != null)
			alert.getDialogPane().setExpandableContent(createExpandableContent(t));
		return alert;
	}
	
	public static Alert warningAlert(String message) {
		return warningAlert(null, message, null);
	}
	
	public static Alert warningAlert(String header, String message) {
		return warningAlert(header, message, null);
	}
	
	public static Alert warningAlert(String header, String message, Throwable t) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Warning");
		alert.setHeaderText(header);
		alert.setContentText(message);
		if(t != null)
			alert.getDialogPane().setExpandableContent(createExpandableContent(t));
		return alert;
	}
	
	public static Alert infoAlert(String message) {
		return infoAlert(null, message);
	}
	
	public static Alert infoAlert(String header, String message) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Info");
		alert.setHeaderText(header);
		alert.setContentText(message);
		return alert;
	}
	
	public static void copyToClipboard(String text) {
		ClipboardContent content = new ClipboardContent();
		content.putString(text);
		Clipboard.getSystemClipboard().setContent(content);
	}
	
	public static File fromFileSystem(Stage owner, String... extensions) {
		return createFileChooser(extensions).showOpenDialog(owner);
	}
	
	public static List<File> fromFileSystemMultiple(Stage owner, String... extensions) {
		return createFileChooser(extensions).showOpenMultipleDialog(owner);
	}
	
	
	private static GridPane createExpandableContent(Throwable t) {
		StringWriter sw = new StringWriter();
		t.printStackTrace(new PrintWriter(sw));
		String stackTrace = sw.toString();
		return createExpandableContent(stackTrace);
	}
	
	private static GridPane createExpandableContent(String content) {
		Label label = new Label("Exception stacktrace");

		TextArea textArea = new TextArea(content);
		textArea.setEditable(false);
		textArea.setWrapText(true);

		textArea.setMaxWidth(Double.MAX_VALUE);
		textArea.setMaxHeight(Double.MAX_VALUE);
		GridPane.setVgrow(textArea, Priority.ALWAYS);
		GridPane.setHgrow(textArea, Priority.ALWAYS);

		GridPane expContent = new GridPane();
		expContent.setMaxWidth(Double.MAX_VALUE);
		expContent.add(label, 0, 0);
		expContent.add(textArea, 0, 1);

		return expContent;
	}
	
	private static FileChooser createFileChooser(String... extensions) {
		FileChooser fc = new FileChooser();
		fc.setTitle("Choose file(s)");
		fc.getExtensionFilters().addAll(Arrays.stream(extensions)
				.map(s -> { return new ExtensionFilter(s.toUpperCase()+" files", "*."+s); })
				.collect(Collectors.toList()));
		return fc;
	}

	
}
