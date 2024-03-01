package org.unibl.etf.pj2.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.unibl.etf.pj2.model.vehicle.Bus;
import org.unibl.etf.pj2.model.vehicle.Car;
import org.unibl.etf.pj2.model.vehicle.Truck;
import org.unibl.etf.pj2.model.vehicle.Vehicle;

import javafx.application.Platform;



public class QueueService {
	
	//Ili samo Liskov Substitution principle
	public static ArrayList<Vehicle> _vehicles = new ArrayList<Vehicle>() ;
	
	public static void initialize() {
		
	}

	private static int sleepTime =100;
	
	public static synchronized void addVehicle(Vehicle v, int index, int rowOnMap) {
		_vehicles.add(index,v);
		_vehicles.get(index).setRowOnMap(rowOnMap);
		Collections.sort(_vehicles);
	}
	
	public static  void startVehicle(int index) throws InterruptedException {
		for(int i=0; i<50;i++) {
			_vehicles.get(i).start();
		}
		
		System.out.println("Koliko je vrijeme spavanja " + sleepTime);
		
	}
	
	public static synchronized void stopVehicleDriving() {
		int index=0;
		while(index < _vehicles.size()) {
			try {
					_vehicles.get(index).set_pauseDriving(true);
					System.out.println("Pauziralo je vozilo : " + _vehicles.get(index).get_sign());
				
			}catch(Exception ex) {
	           Logger.getLogger(Vehicle.class.getName()).log(Level.SEVERE, null, ex);
			}
			index++;
		  }
	}
	
	public synchronized static void notifyVehicle(int index) {
		_vehicles.get(index).set_pauseDriving(false);
		synchronized(_vehicles.get(index)) {
			if(_vehicles.get(index) != null) {
				_vehicles.get(index).notifyAll();
			}
		}
	}
	
	public synchronized static void continueVehicleDriving() {
		int index=0;
		while(index < _vehicles.size()) {
			try {
					_vehicles.get(index).set_pauseDriving(false);
					_vehicles.get(index).resumeFromPause();
					System.out.println("Vozilo " + _vehicles.get(index).get_sign() + " ponovo se krece!");
			
				
			}catch(Exception ex) {
	           Logger.getLogger(Vehicle.class.getName()).log(Level.SEVERE, null, ex);
			}
			index++;
		  }
		
	}
	
	public static Vehicle getVehicleBySign(String sign) {
		System.out.println("Sign ovi unutar metode u Vehicle su: " + _vehicles.get(0).get_sign());

		for(int i=0; i < _vehicles.size(); i++) {
			System.out.println("Sign ovi unutar metode u Vehicle su: " + _vehicles.get(i).get_sign());
			if(_vehicles.get(i).get_sign().equals(sign)) {
				return _vehicles.get(i);
			}
		}
		return null;
		
	}

}
