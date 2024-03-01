package org.unibl.etf.pj2.model.vehicle;

import java.util.ArrayList;
import java.util.Random;
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

public class Truck extends Vehicle implements IType {
	
	private static final int MIN_NUMBER_OF_PASSENGERS = 1;
	private static final int MAX_NUMBER_OF_PASSENGERS = 3;
	
	public static final int _declarativeWeightInKilograms = 5000; //5 tona
	
	public static int _realWeightInKilograms;
	
	public Truck(int _id, int _capacity, String _sign, ArrayList<Person> _passengers, Person _driver, boolean _pauseDriving,boolean _passedPoliceTerminal, boolean _passedCustomsTerminal) {
		super(_id,_capacity, _sign, _passengers, _driver, _pauseDriving,_passedPoliceTerminal, _passedCustomsTerminal);
	}

	public int get_declarativeWeightInKilograms() {
		return _declarativeWeightInKilograms;
	}

	//public void set_declarativeWeight(int _declarativeWeightInKilograms) {
	//	this._declarativeWeightInKilograms = _declarativeWeightInKilograms;
	//}

	public int get_realWeightInKilograms() {
		return _realWeightInKilograms;
	}

	public void set_realWeightInKilograms(int _realWeight) {
		this._realWeightInKilograms = _realWeight;
	}
	
	public boolean checkDocumentationOnBorder() {
		if(_realWeightInKilograms > _declarativeWeightInKilograms) {
			return false;
		}
		
		return true;
		
	}
	
	public static int generateRealDocumentation() {
		Random randomGenerator = new Random();
		int randomNumber = randomGenerator.nextInt(2); //0 treba dokumentacija 1 ne treab
		if(randomNumber == 0) {
			int randomOverweight = isOverweight();
			if(randomOverweight == 1 || randomOverweight==2) {
				int overWeight = generateOverweightRealWeight();
				_realWeightInKilograms = overWeight + _declarativeWeightInKilograms;
				return 1;
			}
		}
		else {
			_realWeightInKilograms = 0;
			
		}
		return 0;
	}
	
	public static int isOverweight() {
		Random randomGenerator = new Random();
		int max = 10;
		int min = 1;
		int isOverweight = randomGenerator.nextInt(max - min + 1) + min; //generise broj od 1 do 10
		return isOverweight;
	}
	public static int generateOverweightRealWeight() {
		Random randomGenerator = new Random();
		int max = 1500;
		int min = 1;
		int overWeight = randomGenerator.nextInt(max - min + 1) + min; //generise broj od 1 do 1500
		return overWeight;
	}
	
	public boolean isImplementingTruckInterface() {
		if(this instanceof Truck) {
			return true;
		}
		return false;
	}

	//1 - Car
	//2 - Bus
	//3 - Truck
	@Override
	public synchronized int typeOfVehicle() {
		return 3;
	}
	

}
