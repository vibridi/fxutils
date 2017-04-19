package com.vibridi.fxu.dialog;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;

public class FXDialog {

	public static Alert errorAlert(String message) {
		return errorAlert(null, message, null);
	}
	
	public static Alert errorAlert(String header, String message) {
		return errorAlert(header, message, null);
	}
	
	public static Alert errorAlert(String message, Throwable t) {
		return errorAlert(null, message, t);
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
	
	public static Alert warningAlert(String message, Throwable t) {
		return warningAlert(null, message, t);
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
	
	public static Alert binaryChoiceAlert(String message) {
		return binaryChoiceAlert(null, message);
	}
	
	public static Alert binaryChoiceAlert(String header, String message) {
		Alert alert = new Alert(AlertType.CONFIRMATION);	
		alert.setTitle("Confirm action");
		alert.setHeaderText(header);
		alert.setContentText(message);
		
		ButtonType[] buttons = new ButtonType[] {
			ButtonType.YES,
			ButtonType.NO
		};
		
		alert.getButtonTypes().setAll(buttons);
		return alert;
	}
	
	public static Alert multipleChoiceAlert(String message, ButtonType... buttons) {
		return multipleChoiceAlert(null, message);
	}
	
	public static Alert multipleChoiceAlert(String header, String message,ButtonType... buttons) {
		Alert alert = new Alert(AlertType.CONFIRMATION);	
		alert.setTitle("Confirm action");
		alert.setHeaderText(header);
		alert.setContentText(message);
		alert.getButtonTypes().setAll(buttons);
		return alert;
	}
	
	public static void saveFile(byte[] bytes, Stage owner, String... extensions) throws IOException {
		File dest = createFileChooser("Save file", extensions).showSaveDialog(owner);
		Files.write(Paths.get(dest.toURI()), bytes);
	}
	
	/**
	 * 
	 * @param owner The node owner of the search dialog
	 * @param extensions File extensions filters. Do not prepend '.' to the extension.
	 * @return
	 */
	public static File openFile(Stage owner, String... extensions) {
		return createFileChooser("Open file", extensions).showOpenDialog(owner);
	}
	
	/**
	 * 
	 * @param owner The node owner of the search dialog
	 * @param extensions File extensions filters. Do not prepend '.' to the extension.
	 * @return
	 */
	public static List<File> openFiles(Stage owner, String... extensions) {
		return createFileChooser("Open files", extensions).showOpenMultipleDialog(owner);
	}
	
	public static List<File> openDirectory(Stage owner) {
		File dir = chooseDirectory(owner);
		if(dir == null)
			return null;
		return Arrays.asList(dir.listFiles());		
	}
	
	public static File chooseDirectory(Stage owner) {
		return createDirectoryChooser("Open directory").showDialog(owner);
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
	
	private static FileChooser createFileChooser(String title, String... extensions) {
		FileChooser fc = new FileChooser();
		fc.setTitle(title);
		fc.getExtensionFilters().addAll(Arrays.stream(extensions)
				.map(s -> { return new ExtensionFilter(s.toUpperCase()+" files", "*."+s); })
				.collect(Collectors.toList()));
		return fc;
	}
	
	private static DirectoryChooser createDirectoryChooser(String title) {
		DirectoryChooser dc = new DirectoryChooser();
		//dc.setInitialDirectory(new File(homeUri));
		dc.setTitle(title);
		return dc;
	}
	
}
