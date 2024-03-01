package org.unibl.etf.pj2.model.vehicle;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.unibl.etf.pj2.controller.MainWindowController;
import org.unibl.etf.pj2.controller.QueueWindowController;
import org.unibl.etf.pj2.file.CustomFileHandler;
import org.unibl.etf.pj2.helper.Constants;
import org.unibl.etf.pj2.model.border.CustomsTerminal;
import org.unibl.etf.pj2.model.border.CustomsTruckTerminal;
import org.unibl.etf.pj2.model.border.PoliceTerminal;
import org.unibl.etf.pj2.model.border.PoliceTruckTerminal;
import org.unibl.etf.pj2.model.person.Person;
import org.unibl.etf.pj2.model.vehicle.interfaces.IType;

import javafx.application.Platform;
import javafx.scene.paint.Color;

public class Bus extends Vehicle implements IType{

	private static final int MIN_NUMBER_OF_PASSENGERS = 1;
	private static final int MAX_NUMBER_OF_PASSENGERS = 51;

	
	//da li mi treba cargo space ? Svakako unutar svakog Person - a postoji kofer
	public Bus(int _id, int _capacity, String _sign, ArrayList<Person> _passengers, Person _driver, boolean _pauseDriving, boolean _passedPoliceTerminal, boolean _passedCustomsTerminal) {
		super(_id,_capacity, _sign, _passengers, _driver, _pauseDriving, _passedPoliceTerminal,_passedCustomsTerminal);
	}

	//1 - Car
	//2 - Bus
	//3 - Truck
	@Override
	public synchronized int typeOfVehicle() {
		return 2;
	}	
	




	
}
