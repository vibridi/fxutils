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
	
	public List<String> getLanguageDisplayNames() {
		return languageDisplayNames;
	}
	
	public StringBinding createStringBinding(final String key, final Object... args) {
		return Bindings.createStringBinding(() -> get(key, args), locale);
	}
	
	public StringBinding createStringBinding(Callable<String> func) {
	    return Bindings.createStringBinding(func, locale);
	}
	
	public void switchLanguage(Locale locale) {
		setLocale(locale);
	}
	
	public void switchLanguage(String value) {
		setLocale(supportedLocales.get(languageDisplayNames.indexOf(value)));
	}
	

	/*********************************************
	 *                                           *
	 * FX PROPERTIES		                     *
	 *                                           *
	 *********************************************/
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
	public void bind(Labeled node, String key, Object... args) {
		node.textProperty().bind(createStringBinding(key, args));
	}
	
	public void bind(MenuItem node, String key, Object... args) {
		node.textProperty().bind(createStringBinding(key, args));
	}
	
	public void bind(TableColumn<?,?> node, String key, Object... args) {
		node.textProperty().bind(createStringBinding(key, args));
	}
	
	public Label getLabel(String key, Object... args) {
		Label lbl = new Label();
		lbl.textProperty().bind(createStringBinding(key, args));
		return lbl;
	}
	
	public Button getButton(String key, Object... args) {
		Button btn = new Button();
		btn.textProperty().bind(createStringBinding(key, args));
		return btn;
	}
	
	public void setTooltip(Node node, String key, Object... args) {
		Tooltip tooltip = new Tooltip();
	    tooltip.textProperty().bind(createStringBinding(key, args));
	    Tooltip.install(node, tooltip);
	}
	
	public String get(final String key, final Object... args) {
		ResourceBundle bundle = ResourceBundle.getBundle(basePath, getLocale(), classLoader);
		return MessageFormat.format(bundle.getString(key), args);
	}
	
}
