package com.vibridi.fxu.keyboard;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyCombination.Modifier;
import javafx.scene.input.KeyEvent;

public class FXKeyboard {
	
	/*********************************************
	 *                                           *
	 * PUBLIC METHODS		                     *
	 *                                           *
	 *********************************************/
	/**
	 * Convenience method for building {@link KeyCombination} objects from a string descriptor.
	 * Supports any combination of Ctrl (or Mac Command), Alt, Shift plus one of the following:
	 * <ul>
	 * <li>letter [A-Z], case insensitive</li>
	 * <li>digit [0-9]</li>
	 * <li>backspace, space, enter</li>
	 * <li>&amp;, ^, *, \, !, +</li>
	 * </ul>
	 * 
	 * @param keyCombination A string representing a key combination
	 * @return A JavaFX <code>KeyCombination</code> object
	 */
	public static KeyCombination buildKeyCombination(String keyCombination) {
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
		
		return new KeyCodeCombination(keyCode, modifiers.toArray(new Modifier[modifiers.size()]));
	}
	
	/**
	 * Convenience method for setting key combination shortcuts.
	 * Supports any combination of Ctrl (Command), Alt, Shift plus one of the following:
	 * <ul>
	 * <li>letter [A-Z], case insensitive</li>
	 * <li>digit [0-9]</li>
	 * <li>backspace, space, enter</li>
	 * <li>&amp;, ^, *, \, !, +</li>
	 * </ul>
	 * 
	 * @param node The node that will respond to the key combination
	 * @param keyCombination A string in the format 'modifier[+modifier]+key'
	 * @param keyHandler The function called when the key combo is detected
	 */
	public static void setKeyCombinationShortcut(Node node, String keyCombination, EventHandler<? super KeyEvent> keyHandler) {
		final KeyCombination combo = buildKeyCombination(keyCombination);
		
		node.addEventHandler(KeyEvent.KEY_RELEASED, event -> {
			if(combo.match(event))
				keyHandler.handle(event);
		});
	}
	
	/*********************************************
	 *                                           *
	 * PRIVATE METHODS		                     *
	 *                                           *
	 *********************************************/
	/**
	 * Converts the textual representation of a key into a JavaFX {@link KeyCode}
	 * @param name Key string
	 * @return JavaFX <code>KeyCode</code> object
	 */
	private static KeyCode asKeyCombinationMain(String name) {
		if(name == null || name.isEmpty())
			throw new IllegalArgumentException("Key definition is null or empty");
		
		name = name.toUpperCase();
		if(name.length() == 1) {
			if(Character.isAlphabetic(name.charAt(0)))
				return KeyCode.valueOf(name);
			if(Character.isDigit(name.charAt(0)))
				return KeyCode.valueOf("DIGIT" + name);
			
			switch(name.charAt(0)) {
			case '&':
				return KeyCode.AMPERSAND;
			case '*':
				return KeyCode.ASTERISK;
			case '^':
				return KeyCode.CIRCUMFLEX;
			case '!':
				return KeyCode.EXCLAMATION_MARK;
			case '\\':
				return KeyCode.BACK_SLASH;
			case '+':
				return KeyCode.PLUS;
			}
			
			throw new IllegalArgumentException("Unknown or unsupported key: " + name);
		}
		
		switch(name) {
		case "BACKSPACE":
			return KeyCode.BACK_SPACE;
		case "SPACE":
		case "SPACEBAR":
			return KeyCode.SPACE;
			
		case "ENTER":
			return KeyCode.ENTER;
			
		default:
			return KeyCode.valueOf(name);
		}
	}
	
	/**
	 * Converts the textual representation of a key modifier into a JavaFX {@link Modifier}. 
	 * Supports CTRL, CMD, ALT, SHIFT
	 * @param name Modifier string
	 * @return JavaFX <code>Modifier</code> object
	 */
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
