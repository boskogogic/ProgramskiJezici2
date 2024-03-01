package org.unibl.etf.pj2.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import org.unibl.etf.pj2.file.CustomFileHandler;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;

public class IncidentWindowController implements Initializable{

	@FXML
	TextArea Incidents;
	
	TextArea incidentsAlternative;
	
    public static int index;
    
    public int i=0;
	
    public ArrayList<String> incidents = CustomFileHandler.readIncidentsFromFile(CustomFileHandler.fileName);
    public ArrayList<String> incidentsFromBinaryFile = CustomFileHandler.readIncidentsFromBinaryFile(CustomFileHandler.binaryFileName);
	
    @Override 
	public void initialize(URL location, ResourceBundle resources) {
	        String Title = "Incidents window";
	      
	        incidentsAlternative = Incidents;
	        index=0;

	        while(i < incidents.size()) {
	        	final int currentIndex = i;
	        	Platform.runLater(()->incidentsAlternative.appendText(incidents.get(currentIndex) + "\n"));
	        	i++;
	        }
	        i=0;
	        while(i < incidentsFromBinaryFile.size()) {
	        	final int currentIndex = i;
	        	Platform.runLater(()->incidentsAlternative.appendText(incidentsFromBinaryFile.get(currentIndex) + "\n"));
	        	i++;
	        }
	    //i=0;
	        
	}
	
}
