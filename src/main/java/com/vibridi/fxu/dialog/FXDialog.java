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

/**
 * Utility and factory methods for JavaFX dialogs. Remember that the majority of these methods only construct che Alert. 
 * You will have to call their relevant show methods for them to appear on the screen.
 *
 */
public class FXDialog {

	/*********************************************
	 *                                           *
	 * ALERTS				                     *
	 *                                           *
	 *********************************************/
	/**
	 * Creates an error alert.
	 * @param message The alert body message
	 * @return A JavafX error alert
	 */
	public static Alert errorAlert(String message) {
		return errorAlert(null, message, null);
	}
	
	public static Alert errorAlert(String header, String message) {
		return errorAlert(header, message, null);
	}
	
	public static Alert errorAlert(String message, Throwable t) {
		return errorAlert(null, message, t);
	}
	
	/**
	 * Creates an error alert with an expandable section that shows a Java stack trace.
	 * @param header The alert header message (this is not the dialog title)
	 * @param message The alert body message
	 * @param t The Java throwable whose stack trace will appear in the expandable section.
	 * @return A JavafX error alert
	 */
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
	
	/**
	 * Creates a YES/NO alert (buttons included).
	 * @param message The alert body message
	 * @return An alert dialog with the Yes and No buttons.
	 */
	public static Alert binaryChoiceAlert(String message) {
		return binaryChoiceAlert(null, message);
	}
	
	/**
	 * Creates a YES/NO alert (buttons included).
	 * @param header The alert header message
	 * @param message The alert body message
	 * @return An alert dialog with the Yes and No buttons.
	 */
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
	
	/*********************************************
	 *                                           *
	 * FILE SYSTEM DIALOGS	                     *
	 *                                           *
	 *********************************************/
	/**
	 * Shows a file system dialog and saves a file to the selected path.
	 * @param bytes The byte content of the file
	 * @param owner The Stage object that will own the dialog
	 * @param extensions The extensions allowed for the file. Do not prepend '.' to the extension
	 * @throws IOException - if it fails to write the file
	 */
	public static void saveFile(byte[] bytes, Stage owner, String... extensions) throws IOException {
		File dest = createFileChooser("Save file", extensions).showSaveDialog(owner);
		Files.write(Paths.get(dest.toURI()), bytes);
	}
	
	/**
	 * Shows a file system dialog that allows selecting a save path.
	 * @param owner The Stage object that will own the dialog
	 * @param extensions The extensions allowed for the file. Do not prepend '.' to the extension
	 * @return File representing the chosen location
	 */
	public static File saveFile(Stage owner, String... extensions) {
		return createFileChooser("Save file", extensions).showSaveDialog(owner);
	}
	
	/**
	 * Shows a file system dialog that allows selecting a file path.
	 * @param owner The Stage object that will own the dialog
	 * @param extensions File extensions filters. Do not prepend '.' to the extension.
	 * @return File representing the chosen location
	 */
	public static File openFile(Stage owner, String... extensions) {
		return createFileChooser("Open file", extensions).showOpenDialog(owner);
	}
	
	/**
	 * Shows a file system dialog that allows selecting multiple file paths.
	 * @param owner The node owner of the search dialog
	 * @param extensions File extensions filters. Do not prepend '.' to the extension.
	 * @return A list of files representing the chosen locations
	 */
	public static List<File> openFiles(Stage owner, String... extensions) {
		return createFileChooser("Open files", extensions).showOpenMultipleDialog(owner);
	}
	
	/**
	 * Shows a file system dialog that allows selecting a directory.
	 * @param owner The node owner of the search dialog
	 * @return A list of the files contained in the chosen location
	 */
	public static List<File> openDirectory(Stage owner) {
		File dir = chooseDirectory(owner);
		if(dir == null)
			return null;
		return Arrays.asList(dir.listFiles());		
	}
	
	/**
	 * Shows a file system dialog that allows selecting a directory.
	 * @param owner The node owner of the search dialog
	 * @return A reference to the chosen directory
	 */
	public static File chooseDirectory(Stage owner) {
		return createDirectoryChooser("Open directory").showDialog(owner);
	}
	
	/*********************************************
	 *                                           *
	 * PRIVATE METHODS		                     *
	 *                                           *
	 *********************************************/
	/**
	 * Creates an expandable content with the stack trace of a throwable
	 * @param t The throwable
	 * @return A GridPane that will be embedded in a JavaFX alert dialog.
	 */
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
		//dc.setInitialDirectory(new File(homeUri)); // TODO 
		dc.setTitle(title);
		return dc;
	}
	
}
