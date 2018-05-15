package com.vibridi.fxu.test;

import java.util.Arrays;
import java.util.List;

import com.vibridi.fxu.builder.FXBuilder;
import com.vibridi.fxu.input.AutoComplete;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

public class FXUMain extends Application {
	
	public static void main(String[] args) {
		launch();
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		List<String> list = Arrays.asList("AGLIE'","AIRASCA","ALA DI STURA","ALBIANO D'IVREA","ALICE SUPERIORE","ALMESE","ALPETTE",
				"ALPIGNANO","ANDEZENO","ANDRATE","ANGROGNA","ARIGNANO","AVIGLIANA","AZEGLIO","BAIRO","BALANGERO",
				"BALDISSERO CANAVESE","BALDISSERO TORINESE","BALME","BANCHETTE","BARBANIA","BARDONECCHIA","BARONE CANAVESE","BEINASCO",
				"BIBIANA","BOBBIO PELLICE","BOLLENGO","BORGARO TORINESE","BORGIALLO","BORGOFRANCO D'IVREA","BORGOMASINO","BORGONE SUSA",
				"BOSCONERO","BRANDIZZO","BRICHERASIO","BROSSO","BROZOLO","BRUINO","BRUSASCO","BRUSASCO","BRUZOLO","BURIASCO","BUROLO",
				"BUSANO","BUSSOLENO","BUTTIGLIERA ALTA","CAFASSE","CALUSO","CAMBIANO","CAMPIGLIONE-FENILE","CANDIA CANAVESE","CANDIOLO",
				"CANISCHIO","CANTALUPA","CANTOIRA","CAPRIE","CARAVINO","CAREMA","CARIGNANO");		
		ComboBox<String> cb = new ComboBox<>();
		cb.setItems(FXCollections.observableArrayList(list));
		
		//AutoComplete.setAutoComplete(cb, (text, s) -> s.toLowerCase().contains(text.toLowerCase()));
		
		FXBuilder.justShow(cb, 10.0, null, null, 10.0);
	}
	
}
