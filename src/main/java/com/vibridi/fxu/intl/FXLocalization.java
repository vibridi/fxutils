package com.vibridi.fxu.intl;

import java.text.MessageFormat;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Tooltip;

public class FXLocalization {
	
	/**
	 * Converts BCP 47 tags into {@link Locale} objects.
	 * 
	 * @param supportedLocaleTags The tags of the languages that your application supports.
	 * @return A list of corresponding <code>Locale</code> objects
	 */
	public static List<Locale> parseBCP47Tags(String[] supportedLocaleTags) {
		return Stream.of(supportedLocaleTags)
				.map(tag -> Locale.forLanguageTag(tag.replace("_", "-")))
				.filter(loc -> !loc.toLanguageTag().equals("und"))	// TODO improve error checking
				.collect(Collectors.toList());
	}

	private ObjectProperty<Locale> locale;
	private List<Locale> supportedLocales;
	private List<String> languageDisplayNames;
	private String basePath;
	private ClassLoader classLoader;
	
	public FXLocalization(String[] supportedLocaleTags, String basePath, ClassLoader classLoader) {
		this(parseBCP47Tags(supportedLocaleTags), basePath, classLoader);
	}
	
	/**
	 * Initializes an FXLocalization object that manages the resource bundle of your app for switching between languages.
	 * See also {@link ResourceBundle}.
	 * 
	 * @param supportedLocales A list of the language locales supported by the client app.
	 * @param basePath Base path used to load the resource bundles. 
	 * @param classLoader Class loader used to load the resource bundles.
	 */
	public FXLocalization(List<Locale> supportedLocales, String basePath, ClassLoader classLoader) {
		if(supportedLocales.isEmpty())
			supportedLocales.add(Locale.getDefault());
		
		this.supportedLocales = supportedLocales;
		this.basePath = basePath;
		this.classLoader = classLoader;
		
		languageDisplayNames = supportedLocales.stream()
				.map(loc -> loc.getDisplayLanguage(loc).toUpperCase())
				.collect(Collectors.toList());
		
		locale = new SimpleObjectProperty<>(getDefaultLocale());
		locale.addListener((obs,o,n) -> Locale.setDefault(n));
	}
	
	public Locale getDefaultLocale() {
		Locale sysDefault = Locale.getDefault();
		return getSupportedLocales().contains(sysDefault) ? sysDefault : supportedLocales.get(0);
	}
	
	public List<Locale> getSupportedLocales() {
		return supportedLocales;
	}
	
	/**
	 * @return The display names of the supported languages.
	 */
	public List<String> getLanguageDisplayNames() {
		return languageDisplayNames;
	}
	
	public String getCurrentLanguage() {
		return getLocale().getDisplayLanguage(getLocale()).toUpperCase();
	}
	
	/**
	 * Binds a localizable string to the currently selected locale. When the locale changes, the bound string will be updated 
	 * in real time with the correct translation.
	 *  
	 * @param key The key of the localizable string as found in the resource bundle.
	 * @param args Additional arguments for the {@link MessageFormat#format} method
	 * @return A <code>StringBinding</code> object 
	 */
	public StringBinding createStringBinding(final String key, final Object... args) {
		return Bindings.createStringBinding(() -> get(key, args), locale);
	}
	
	public StringBinding createStringBinding(Callable<String> func) {
	    return Bindings.createStringBinding(func, locale);
	}
	
	/**
	 * Switches the current app language to the new one.
	 * @param locale The <code>Locale</code> object representing the new language.
	 */
	public void switchLanguage(Locale locale) {
		setLocale(locale);
	}
	
	/**
	 * Switches the current app language to the new one. 
	 * You might want to call {@link FXLocalization#getLanguageDisplayNames} to get the exact values you can call this method with.
	 * @param value The display name of the language you want to switch to 
	 */
	public void switchLanguage(String value) {
		setLocale(supportedLocales.get(languageDisplayNames.indexOf(value)));
	}
	

	/*********************************************
	 *                                           *
	 * FX PROPERTIES		                     *
	 *                                           *
	 *********************************************/
	/**
	 * Locale property.
	 * @return Locale property
	 */
	public ObjectProperty<Locale> localeProperty() {
		return locale;
	}

	public Locale getLocale() {
		return localeProperty().get();
	}

	public void setLocale(final Locale locale) {
		localeProperty().set(locale);
	}
	
	
	/*********************************************
	 *                                           *
	 * FACTORY METHODS		                     *
	 *                                           *
	 *********************************************/
	/**
	 * Binds a labeled node to the given resource to reflect language changes in real time.
	 * @param node The labeled node
	 * @param key The resource key
	 * @param args Optional arguments
	 */
	public void bind(Labeled node, String key, Object... args) {
		node.textProperty().bind(createStringBinding(key, args));
	}
	
	/**
	 * Binds a labeled node to the given resource to reflect language changes in real time.
	 * @param node The menu item node
	 * @param key The resource key
	 * @param args Optional arguments
	 */
	public void bind(MenuItem node, String key, Object... args) {
		node.textProperty().bind(createStringBinding(key, args));
	}
	
	/**
	 * Binds a table column to the given resource to reflect language changes in real time.
	 * @param node The table column
	 * @param key The resource key
	 * @param args Optional arguments
	 */
	public void bind(TableColumn<?,?> node, String key, Object... args) {
		node.textProperty().bind(createStringBinding(key, args));
	}
	
	/**
	 * Builds a localized <code>Label</code>.
	 * @param key The resource key
	 * @param args Optional arguments
	 * @return The localized <code>Label</code> object
	 */
	public Label getLabel(String key, Object... args) {
		Label lbl = new Label();
		lbl.textProperty().bind(createStringBinding(key, args));
		return lbl;
	}
	
	/**
	 * Builds a localized <code>Button</code>.
	 * @param key The resource key
	 * @param args Optional arguments
	 * @return The localized <code>Button</code>
	 */
	public Button getButton(String key, Object... args) {
		Button btn = new Button();
		btn.textProperty().bind(createStringBinding(key, args));
		return btn;
	}
	
	/**
	 * Sets a localized tooltip on the given node.
	 * @param node Node that will show the localized tooltip
	 * @param key The resource key
	 * @param args Optional arguments
	 */
	public void setTooltip(Node node, String key, Object... args) {
		Tooltip tooltip = new Tooltip();
	    tooltip.textProperty().bind(createStringBinding(key, args));
	    Tooltip.install(node, tooltip);
	}
	
	/**
	 * Returns the resource corresponding to the given resource in the current locale.
	 * @param key The resource key
	 * @param args Optional arguments
	 * @return The resource in the given locale. 
	 */
	public String get(final String key, final Object... args) {
		ResourceBundle bundle = ResourceBundle.getBundle(basePath, getLocale(), classLoader);
		return MessageFormat.format(bundle.getString(key), args);
	}
	
}
