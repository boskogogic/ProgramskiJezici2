package org.unibl.etf.pj2.controller;

import javafx.event.ActionEvent;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import org.unibl.etf.pj2.model.person.Person;
import org.unibl.etf.pj2.model.vehicle.Vehicle;
import org.unibl.etf.pj2.service.QueueService;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class InformationAboutVehicleWindowController implements Initializable {

	@FXML
	Button Button_Show;
	
	@FXML
	TextField SIGN_TEXT_FIELD;
	

	
	@FXML
	public void onClick_Button_Show(ActionEvent event) {
		String sign = SIGN_TEXT_FIELD.getText();
		System.out.println("Sign je : " + sign);
	
		if(sign.isEmpty()) {
			Alert warning = createAlertEmptySignField();
			warning.show();
		}
		else{
			Vehicle v = QueueService.getVehicleBySign(sign);
			if(v == null) {
				Alert wrongSign = createAlertWrongSign();
				wrongSign.show();
			}
			else {
				Alert getVehicleSucess = createAlertGetVehicleSucess(v.get_capacity(), v.get_sign(), v.get_driver()); 
				getVehicleSucess.show();
			}
			
		}
		
	}
	
	private Alert createAlertGetVehicleSucess(int capacity, String sign, Person driver) {
		Alert getVehicleSucess = new Alert(Alert.AlertType.INFORMATION);
		getVehicleSucess.setTitle("Vehicle information");
		getVehicleSucess.setContentText("Vehicle " + sign + " has capacity " + capacity + " and driver is " + driver.get_id());
		return getVehicleSucess;
	}
	
	private Alert createAlertWrongSign() {
		Alert wrongSign = new Alert(Alert.AlertType.INFORMATION);
		wrongSign.setTitle("Wrong sign");
		wrongSign.setContentText("Vehicle with that sign doesn't exist!");
		return wrongSign;
	}
	
	private Alert createAlertEmptySignField() {
		Alert warning = new Alert(Alert.AlertType.INFORMATION);
		warning.setTitle("Wrong vehicle sign warning");
		warning.setContentText("Vehicle sign incorrect!");
		return warning;
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}
}
