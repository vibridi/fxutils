package com.vibridi.fxu.keyboard;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.vibridi.fxu.FXUtils;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCombination.Modifier;

public class FXKeyboard {
	/**
	 * Convenience method for setting key combination shortcuts.
	 * Supports any combination of Ctrl (Command on Mac), Alt, Shift plus letters or digits.
	 * 
	 * @param scene The node that will respond to the key combination
	 * @param keyCombination A string in the format 'modifier[+modifier]+key'
	 * @param keyHandler
	 */
	public static void setKeyCombinationShortcut(Node node, String keyCombination, EventHandler<? super KeyEvent> keyHandler) {
		keyCombination = keyCombination.toLowerCase();
		List<String> keys = Arrays.asList(keyCombination.split("\\+"));
		if(keys.size() < 1)
			throw new IllegalArgumentException("Key combination is empty");
		
		String stringCode = keys.get(keys.size() - 1);
		KeyCode keyCode = asKeyCombinationMain(stringCode);
		
		List<Modifier> modifiers = keys.stream()
				.limit(keys.size() - 1)
				.distinct()
				.map(FXKeyboard::asKeyCombinationModifier)
				.collect(Collectors.toList());	
		
		final KeyCombination combo = new KeyCodeCombination(keyCode, modifiers.toArray(new Modifier[modifiers.size()]));
		
		node.addEventHandler(KeyEvent.KEY_RELEASED, event -> {
			if(combo.match(event))
				keyHandler.handle(event);
		});
	}
	
	private static KeyCode asKeyCombinationMain(String name) {
		if(name == null || name.isEmpty())
			throw new IllegalArgumentException("Key definition is null or empty");
		
		name = name.toUpperCase();
		if(name.length() == 1) {
			if(Character.isAlphabetic(name.charAt(0)))
				return KeyCode.valueOf(name);
			if(Character.isDigit(name.charAt(0)))
				return KeyCode.valueOf("DIGIT" + name);
//			switch(name.charAt(0)) {
//			
//			}
		}
		throw new IllegalArgumentException("Unknown or unsupported key");
	}
	
	private static Modifier asKeyCombinationModifier(String name) {
		switch(name.toUpperCase()) {
		case "CTRL":
		case "CMD":
			return KeyCombination.SHORTCUT_DOWN;
		case "ALT":
			return KeyCombination.ALT_DOWN;
		case "SHIFT":
			return KeyCombination.SHIFT_DOWN;
		}
		throw new IllegalArgumentException("Unknown or unsupported key modifier");
	}
}
