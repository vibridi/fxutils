package com.vibridi.fxu.launcher;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;

/**
 * Provides access to a JavaFX Application running in a separate event thread. 
 * 
 * @author gabriele.vaccari
 *
 */
public class FXLauncher {
	
	public interface ILauncher {
		public void launch();
	}
	
	// Pugh's singleton
	private static class LauncherHolder {
		private static FXLauncher instance = new FXLauncher();
	}
	
	public static FXLauncher getInstance() {
		return LauncherHolder.instance;
	}
	
	public static void init(Class<? extends Application> clazz) {
		FXLauncher.clazz = clazz;
	}
	
	private static Class<? extends Application> clazz;
	
	private FXLauncher() {
		Platform.setImplicitExit(false);
		final JFXPanel panel = new JFXPanel();
		new Thread(() -> {
			Application.launch(clazz == null ? AsynchronousApplication.class : clazz);
		}).start();
	}
	
	public void submit(ILauncher launcher) {
		Platform.runLater(() -> launcher.launch());
	}
	
}
