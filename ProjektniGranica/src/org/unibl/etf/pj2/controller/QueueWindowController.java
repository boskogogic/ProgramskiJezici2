package org.unibl.etf.pj2.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class QueueWindowController {
	
	@FXML
	private TextArea OtherVehicles;
	
	public static TextArea OtherVehiclesAlternative;
	
	@FXML
	private void initialize() {
		setOtherVehiclesAlternative(OtherVehicles);
	}
	
    public static void setOtherVehiclesAlternative(TextArea otherVehicles) {
        OtherVehiclesAlternative = otherVehicles;
    }

}
