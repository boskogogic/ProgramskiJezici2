package org.unibl.etf.pj2.model.vehicle;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.unibl.etf.pj2.controller.MainWindowController;
import org.unibl.etf.pj2.controller.QueueWindowController;
import org.unibl.etf.pj2.file.CustomFileHandler;
import org.unibl.etf.pj2.model.border.CustomsTerminal;
import org.unibl.etf.pj2.model.border.CustomsTruckTerminal;
import org.unibl.etf.pj2.model.border.PoliceTerminal;
import org.unibl.etf.pj2.model.border.PoliceTruckTerminal;
import org.unibl.etf.pj2.model.person.Person;
import org.unibl.etf.pj2.model.vehicle.interfaces.IType;
import org.unibl.etf.pj2.helper.Constants;
import org.unibl.etf.pj2.helper.Randomise;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Insets;

public abstract class Vehicle extends Thread implements Serializable, IType, Comparable<Vehicle> {

	private int _id;

	protected int _capacity;

	protected String _sign;

	protected ArrayList<Person> _passengers;

	protected Person _driver;

	// Preko statickog dijeljenog objektya
	// neki objekat koji se sinhronizuje
	// i preko njega se i notifaj

	public static boolean _pauseDriving = false;

	protected boolean _passedPoliceTerminal = false;

	protected boolean _passedCustomsTerminal = false;

	protected int rowOnMap;

	protected int columnOnMap = 1;

	protected boolean isRunning = true;

	public static final Object pauseLock = new Object();

	public Vehicle() {

	}

	public Vehicle(int _id, int _capacity, String _sign, ArrayList<Person> _passengers, Person _driver,
			boolean _pauseDriving, boolean _passedPoliceTerminal, boolean _passedCustomsTerminal) {
		super();
		this._id = _id;
		this._capacity = _capacity;
		this._sign = _sign;
		this._passengers = _passengers;
		this._driver = _passengers.get(_capacity - 1);// driver is the last passenger
		this._pauseDriving = _pauseDriving;
		this._passedPoliceTerminal = _passedPoliceTerminal;
		this._passedCustomsTerminal = _passedCustomsTerminal;
	}

	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	public int get_capacity() {
		return _capacity;
	}

	public void set_capacity(int _capacity) {
		this._capacity = _capacity;
	}

	public String get_sign() {
		return _sign;
	}

	public void set_sign(String _sign) {
		this._sign = _sign;
	}

	public ArrayList<Person> get_passengers() {
		return _passengers;
	}

	public void set_passengers(ArrayList<Person> _passengers) {
		this._passengers = _passengers;
	}

	public Person get_driver() {
		return _driver;
	}

	public void set_driver(Person _driver) {
		this._driver = _driver;
	}

	public int getRowOnMap() {
		return rowOnMap;
	}

	public void setRowOnMap(int rowOnMap) {
		this.rowOnMap = rowOnMap;
	}

	public boolean is_passedPoliceTerminal() {
		return _passedPoliceTerminal;
	}

	public void set_passedPoliceTerminal(boolean _passedPoliceTerminal) {
		this._passedPoliceTerminal = _passedPoliceTerminal;
	}

	public boolean is_pauseDriving() {
		return _pauseDriving;
	}

	public void set_pauseDriving(boolean _pauseDriving) {
		this._pauseDriving = _pauseDriving;
	}

	public int getColumnOnMap() {
		return columnOnMap;
	}

	public void setColumnOnMap(int columnOnMap) {
		this.columnOnMap = columnOnMap;
	}

	public boolean is_passedCustomsTerminal() {
		return _passedCustomsTerminal;
	}

	public void set_passedCustomsTerminal(boolean _passedCustomsTerminal) {
		this._passedCustomsTerminal = _passedCustomsTerminal;
	}

	@Override
	public String toString() {
		return "Vehicle " + Vehicle.class.getName() + " [_capacity=" + _capacity + ", _sign=" + _sign + ", _passengers="
				+ _passengers + "]";
	}

	public void run() {
		System.out.println("Vozilo " + this.get_sign() + " pokrenuto");
		try {

			/**
			 * Prvi slucaj -> Vozilo ima slobodno mjesto ispred sebe a nije u redu za
			 * terminale
			 */
			while (!this.is_passedCustomsTerminal() && this.isRunning) {

				if (!this.is_passedPoliceTerminal()) {
					/** Zauzmi naredno polje */
					if ((this.typeOfVehicle() == 1 || this.typeOfVehicle() == 2 || this.typeOfVehicle() == 3)
							&& this.rowOnMap != 2 ) {
						if (isFreeNextPlace(rowOnMap, columnOnMap) && !this._pauseDriving) {
							takePlaceOnMap(rowOnMap, columnOnMap);
							System.out.println("Vozilo " + this.get_sign() + " se nalazi na poziciji "
									+ this.getRowOnMap() + " " + this.getColumnOnMap() + " a na mapi se nalazi "
									+ MainWindowController.map[this.getRowOnMap()][this.getColumnOnMap()]);
							if (this.rowOnMap > 1 && this.rowOnMap < 7) {
								setSignOnLabel(rowOnMap, columnOnMap);
							}
							Thread.sleep(500);

						} else {
							Thread.sleep(420);
						}
					} else if ((this.typeOfVehicle() == 3) && this.rowOnMap == 2
							&& isFreePTK(rowOnMap, columnOnMap) ) {

						takePTKOnMap(rowOnMap, columnOnMap);
						setSignOnLabel(rowOnMap, columnOnMap);
						System.out.println("Vozilo " + this.get_sign() + " se nalazi na poziciji " + this.getRowOnMap()
								+ " " + this.getColumnOnMap() + " a na mapi se nalazi "
								+ MainWindowController.map[this.getRowOnMap()][this.getColumnOnMap()]);
						checkPassengersDocuments(_passengers, _passengers.get(_capacity - 1));
						Thread.sleep(this.get_capacity() * 500);
						while (!this._passedCustomsTerminal /* && !this._pauseDriving */) {
							if ((this.typeOfVehicle() == 3) && this._passedPoliceTerminal
									&& isFreeCTK(rowOnMap, columnOnMap)) {
								takeCTKOnMap(rowOnMap, columnOnMap);
								setSignOnLabel(rowOnMap, columnOnMap);
								checkTruckDocumentation();
								int sleepTime = _capacity * 500;
								Thread.sleep(sleepTime);
								freeCTK(rowOnMap, columnOnMap);
								this.isRunning = false;
							}
						}
						freePoliceTruckTerminal(rowOnMap, columnOnMap);
						this.isRunning = false;
					}
					/**
					 * Drugi slucaj -> Auto ili autobus je medju prva 3 vozila u redu i slobodan je
					 * policijski terminal
					 */
					else if ((this.typeOfVehicle() == 1 || this.typeOfVehicle() == 2) && this.rowOnMap == 2
							&& isFreePoliceTerminal(rowOnMap, columnOnMap) ) {
						if (isFreePT1FromFirstPlace(rowOnMap, columnOnMap)) {
							takePT1OnMapFromFirstPlace(rowOnMap, columnOnMap);
							checkPassengersDocuments(_passengers, _passengers.get(_capacity - 1));
							setSignOnLabel(rowOnMap, columnOnMap);
							System.out.println("Vozilo " + this.get_sign() + " se nalazi na poziciji "
									+ this.getRowOnMap() + " " + this.getColumnOnMap() + " a na mapi se nalazi "
									+ MainWindowController.map[this.getRowOnMap()][this.getColumnOnMap()]);
							if (this.typeOfVehicle() == 1) {
								int sleepTime = _capacity * 500;
								Thread.sleep(sleepTime);
							} else {
								int sleepTime = _capacity * 100;
								Thread.sleep(sleepTime);
							}
							while (!this._passedCustomsTerminal ) {
								if (isFreeCT1(rowOnMap, columnOnMap)) {
									takeCT1OnMap(rowOnMap, columnOnMap);
									setSignOnLabel(rowOnMap, columnOnMap);
									if (this.typeOfVehicle() == 1) {
										int sleepTime = _capacity * 2000;
										Thread.sleep(sleepTime);
									} else {
										checkPassengersSuitcase(_passengers, _passengers.get(_capacity - 1));
										int sleepTime = _capacity * 500;
										Thread.sleep(sleepTime);
									}

									freeCT1(rowOnMap, columnOnMap);
									this.isRunning = false;
								}
							}
							freePoliceTerminal(rowOnMap, columnOnMap);
						}
						/**
						 * Treci slucaj -> Auto ili autobus je medju prva 3 vozila u redu i zauzet je PT1 
						 * ali slobodan je PT2
						 */
						else if (!isFreePT1FromFirstPlace(rowOnMap, columnOnMap)
								&& isFreePT2FromFirstPlace(rowOnMap, columnOnMap)) {
							takePT2OnMapFromFirstPlace(rowOnMap, columnOnMap);
							checkPassengersDocuments(_passengers, _passengers.get(_capacity - 1));
							setSignOnLabel(rowOnMap, columnOnMap);
							System.out.println("Vozilo " + this.get_sign() + " se nalazi na poziciji "
									+ this.getRowOnMap() + " " + this.getColumnOnMap() + " a na mapi se nalazi "
									+ MainWindowController.map[this.getRowOnMap()][this.getColumnOnMap()]);
							if (this.typeOfVehicle() == 1) {
								int sleepTime = _capacity * 500;
								Thread.sleep(sleepTime);
							} else {
								int sleepTime = _capacity * 100;
								Thread.sleep(sleepTime);
							}
							while (!this._passedCustomsTerminal ) {
								if ( isFreeCT1(rowOnMap, columnOnMap)) {
									takeCT1OnMap(rowOnMap, columnOnMap);
									setSignOnLabel(rowOnMap, columnOnMap);
									if (this.typeOfVehicle() == 1) {
										int sleepTime = _capacity * 200;
										Thread.sleep(sleepTime);
									} else {
										checkPassengersSuitcase(_passengers, _passengers.get(_capacity - 1));
										int sleepTime = _capacity * 200;
										Thread.sleep(sleepTime);
									}
									freeCT1(rowOnMap, columnOnMap);
									this.isRunning = false;
								}
							}
							freePoliceTerminal(rowOnMap, columnOnMap);
						}
					} else {
						/** Thred se uspava ukoliko je onemogucen da se krece */
						Thread.sleep(2000);
					}
				}

				synchronized (pauseLock) {
					if (_pauseDriving) {
						try {

							System.out.println("Vozilo " + this.get_sign() + " ceka!");
							pauseLock.wait();
							System.out.println("Vozilo " + this.get_sign() + " probudjeno iz cekanja!");
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();

						}
					}
				}
			}
		} catch (InterruptedException ex) {
			Logger.getLogger(Vehicle.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	/**
	 * Provjera sa prvog mjesta cekanja ili sa drugog mjesta cekanja - da li je
	 * slobodan policijski terminal
	 */
	private boolean isFreePoliceTerminal(int rowOnMap, int columnOnMap) {

		/** Problem -> Kada nije slobodan PT1 a jeste PT2 
		 * 	Izvrsi provjeru i za PT1 ( a na njemu je auto) i baci class cast exception
		 * 	Moram razdvojiti u 2 if - a zbog toga
		 */
		if (rowOnMap == 2 && (MainWindowController.map[rowOnMap - 1][columnOnMap] instanceof PoliceTerminal)) {
			System.out.println("isFreePoliceTerminal : rowOnMap and columnOnMap is " + rowOnMap + " " + columnOnMap);
			if (((PoliceTerminal) MainWindowController.map[rowOnMap - 1][columnOnMap]).is_isItOpenc()) {
				return true;
			}	
		}
		
		if(rowOnMap == 2 && (MainWindowController.map[rowOnMap - 1][columnOnMap - 1] instanceof PoliceTerminal)) {
			if(((PoliceTerminal) MainWindowController.map[rowOnMap - 1][columnOnMap - 1]).is_isItOpenc()) {
				return true;
			}
		}
		return false;
	}

	private boolean isFreePT1FromFirstPlace(int rowOnMap, int columnOnMap) {
		if (MainWindowController.map[rowOnMap - 1][columnOnMap - 1] instanceof PoliceTerminal) {
			PoliceTerminal pt1 = (PoliceTerminal) MainWindowController.map[rowOnMap - 1][columnOnMap - 1];
			if (pt1.is_isItOpenc())
			return true;
		}
		return false;
	}

	private boolean isFreePT2FromFirstPlace(int rowOnMap, int columnOnMap) {
		if (MainWindowController.map[rowOnMap - 1][columnOnMap] instanceof PoliceTerminal) {
			return true;
		}
		return false;
	}

	private boolean isFreePT1FromSecondPlace(int rowOnMap, int columnOnMap) {
		if (rowOnMap < 3) {
			return false;
		}

		if (MainWindowController.map[rowOnMap - 2][columnOnMap - 1] instanceof PoliceTerminal) {
			return true;
		}

		return false;
	}

	private boolean isFreePT2FromSecondPlace(int rowOnMap, int columnOnMap) {
		if (rowOnMap < 3) {
			return false;
		}

		if (MainWindowController.map[rowOnMap - 2][columnOnMap] instanceof PoliceTerminal) {
			return true;
		}
		return false;
	}


	/** Ista provjera za kamione */

	private boolean isFreePTK(int rowOnMap, int columnOnMap) {
		if (MainWindowController.map[rowOnMap - 1][columnOnMap + 1] instanceof PoliceTruckTerminal) {
			PoliceTruckTerminal pt1 = (PoliceTruckTerminal) MainWindowController.map[rowOnMap - 1][columnOnMap + 1];
			if (pt1.is_isItOpenc())
				return true;
		}
		return false;
	}

	private synchronized boolean isFreeNextPlace(int rowOnMap, int columnOnMap) {
		if (MainWindowController.map[rowOnMap - 1][columnOnMap] == Constants.FREE_PLACE) {
			return true;
		}
		return false;
	}

	private synchronized void takePlaceOnMap(int rowOnMap, int columnOnMap) {
		MainWindowController.map[rowOnMap - 1][columnOnMap] = this;
		MainWindowController.map[rowOnMap][columnOnMap] = Constants.FREE_PLACE;

		if (rowOnMap > 1 && rowOnMap < 7) {
			setSignOnLabel(rowOnMap, columnOnMap);
			if (rowOnMap == 3) {
				Platform.runLater(() -> MainWindowController.Queue2LabelAlternative.setText("Q2"));
				Platform.runLater(() -> MainWindowController.Queue2LabelAlternative.setTextFill(Color.WHITE));

			}

			if (rowOnMap == 4) {
				Platform.runLater(() -> MainWindowController.Queue3LabelAlternative.setText("Q3"));
				Platform.runLater(() -> MainWindowController.Queue3LabelAlternative.setTextFill(Color.WHITE));

			}
			if (rowOnMap == 5) {
				Platform.runLater(() -> MainWindowController.Queue4LabelAlternative.setText("Q4"));
				Platform.runLater(() -> MainWindowController.Queue4LabelAlternative.setTextFill(Color.WHITE));

			}

			if (rowOnMap == 6) {
				Platform.runLater(() -> MainWindowController.Queue5LabelAlternative.setText("Q5"));
				Platform.runLater(() -> MainWindowController.Queue5LabelAlternative.setTextFill(Color.WHITE));

			}
		}

		if (rowOnMap > 7) {
			Platform.runLater(
					() -> QueueWindowController.OtherVehiclesAlternative.appendText("Vozilo " + this.get_sign()
							+ " se nalazi na poziciji " + this.getRowOnMap() + " " + this.getColumnOnMap() + "\n"));
		}

		this.setRowOnMap(rowOnMap - 1);
	}

	private void takePT1OnMapFromFirstPlace(int rowOnMap, int columnOnMap) {
		MainWindowController.map[rowOnMap - 1][columnOnMap - 1] = this;
		MainWindowController.map[rowOnMap][columnOnMap] = Constants.FREE_PLACE;
		Platform.runLater(() -> MainWindowController.Queue1LabelAlternative.setText("Q1"));
		Platform.runLater(() -> MainWindowController.Queue1LabelAlternative.setTextFill(Color.WHITE));

		this.set_passedPoliceTerminal(true);
		this.setColumnOnMap(columnOnMap - 1);
		this.setRowOnMap(rowOnMap - 1);
	}

	private void takePT2OnMapFromFirstPlace(int rowOnMap, int columnOnMap) {
		MainWindowController.map[rowOnMap - 1][columnOnMap] = this;
		MainWindowController.map[rowOnMap][columnOnMap] = Constants.FREE_PLACE;
		Platform.runLater(() -> MainWindowController.Queue1LabelAlternative.setText("Q1"));
		Platform.runLater(() -> MainWindowController.Queue1LabelAlternative.setTextFill(Color.WHITE));

		this.set_passedPoliceTerminal(true);
		this.setRowOnMap(rowOnMap - 1);
	}

	private void takePT1OnMapFromSecondPlace(int rowOnMap, int columnOnMap) {
		MainWindowController.map[rowOnMap - 2][columnOnMap - 1] = this;
		MainWindowController.map[rowOnMap][columnOnMap] = Constants.FREE_PLACE;
		Platform.runLater(() -> MainWindowController.Queue2LabelAlternative.setText("Q2"));
		Platform.runLater(() -> MainWindowController.Queue2LabelAlternative.setTextFill(Color.WHITE));

		this.set_passedPoliceTerminal(true);
		this.setColumnOnMap(columnOnMap - 1);
		this.setRowOnMap(rowOnMap - 2);
	}

	private void takePT2OnMapFromSecondPlace(int rowOnMap, int columnOnMap) {
		MainWindowController.map[rowOnMap - 2][columnOnMap] = this;
		MainWindowController.map[rowOnMap][columnOnMap] = Constants.FREE_PLACE;
		Platform.runLater(() -> MainWindowController.Queue2LabelAlternative.setText("Q2"));
		Platform.runLater(() -> MainWindowController.Queue2LabelAlternative.setTextFill(Color.WHITE));

		this.set_passedPoliceTerminal(true);
		this.setRowOnMap(rowOnMap - 2);
	}

	private void takePT1OnMapFromThirdPlace(int rowOnMap, int columnOnMap) {
		MainWindowController.map[rowOnMap - 3][columnOnMap - 1] = this;
		MainWindowController.map[rowOnMap][columnOnMap] = Constants.FREE_PLACE;
		Platform.runLater(() -> MainWindowController.Queue3LabelAlternative.setText("Q3"));
		Platform.runLater(() -> MainWindowController.Queue3LabelAlternative.setTextFill(Color.WHITE));

		this.set_passedPoliceTerminal(true);
		this.setColumnOnMap(columnOnMap - 1);
		this.setRowOnMap(rowOnMap - 3);
	}

	private void takePT2OnMapFromThirdPlace(int rowOnMap, int columnOnMap) {
		MainWindowController.map[rowOnMap - 3][columnOnMap] = this;
		MainWindowController.map[rowOnMap][columnOnMap] = Constants.FREE_PLACE;
		Platform.runLater(() -> MainWindowController.Queue3LabelAlternative.setText("Q3"));
		Platform.runLater(() -> MainWindowController.Queue3LabelAlternative.setTextFill(Color.WHITE));

		this.set_passedPoliceTerminal(true);
		this.setRowOnMap(rowOnMap - 3);
	}

	private void takePTKOnMap(int rowOnMap, int columnOnMap) {

		if (rowOnMap == 2) {
			MainWindowController.map[rowOnMap - 1][columnOnMap + 1] = this;
			MainWindowController.map[rowOnMap][columnOnMap] = Constants.FREE_PLACE;
			Platform.runLater(() -> MainWindowController.Queue1LabelAlternative.setText("Q1"));
			Platform.runLater(() -> MainWindowController.Queue1LabelAlternative.setTextFill(Color.WHITE));

			this.set_passedPoliceTerminal(true);
			this.setRowOnMap(rowOnMap - 1);
			this.setColumnOnMap(columnOnMap + 1);
		}

	}

	private synchronized void freePoliceTerminal(int rowOnMap, int columnOnMap) {
		if (columnOnMap == 0) {
			MainWindowController.map[rowOnMap][columnOnMap] = MainWindowController.PT1Object;
			Platform.runLater(() -> MainWindowController.PT1LabelAlternative.setText("PT1"));
			Platform.runLater(() -> MainWindowController.PT1LabelAlternative.setTextFill(Color.WHITE));

		} else {
			MainWindowController.map[rowOnMap][columnOnMap] = MainWindowController.PT2Object;
			Platform.runLater(() -> MainWindowController.PT2LabelAlternative.setText("PT2"));
			Platform.runLater(() -> MainWindowController.PT2LabelAlternative.setTextFill(Color.WHITE));

		}
	}

	private void freePoliceTruckTerminal(int rowOnMap, int columnOnMap) {
		MainWindowController.map[rowOnMap][columnOnMap] = MainWindowController.PTKObject;
		Platform.runLater(() -> MainWindowController.PTKLabelAlternative.setText("PTK"));
		Platform.runLater(() -> MainWindowController.PTKLabelAlternative.setTextFill(Color.WHITE));

	}

	private synchronized boolean isFreeCT1(int rowOnMap, int columnOnMap) {

		// From PT2
		if (columnOnMap == 1 && rowOnMap == 1) {
			if (MainWindowController.map[rowOnMap - 1][columnOnMap - 1] instanceof CustomsTerminal) {
				if(((CustomsTerminal)MainWindowController.map[rowOnMap - 1][columnOnMap - 1])._isItOpenc()) {
					return true;
				}
				
			}
		}

		// From PT1
		if (columnOnMap == 0 && rowOnMap == 1) {
			if (MainWindowController.map[rowOnMap - 1][columnOnMap] instanceof CustomsTerminal) {
				if(((CustomsTerminal)MainWindowController.map[rowOnMap - 1][columnOnMap])._isItOpenc()) {
					return true;
				}
			}
		}

		return false;
	}

	private void freeCT1(int rowOnMap, int columnOnMap) {
		MainWindowController.map[rowOnMap][columnOnMap] = MainWindowController.CT1Object;
		Platform.runLater(() -> MainWindowController.CTLabelAlternative.setText("CT"));
		Platform.runLater(() -> MainWindowController.CTLabelAlternative.setTextFill(Color.WHITE));

	}

	private synchronized void takeCT1OnMap(int rowOnMap, int columnOnMap) {
		// From PT2
		if (columnOnMap == 1) {
			MainWindowController.map[rowOnMap - 1][columnOnMap - 1] = this;
			MainWindowController.map[rowOnMap][columnOnMap] = MainWindowController.PT2Object;
			Platform.runLater(() -> MainWindowController.PT2LabelAlternative.setText("PT2"));
			Platform.runLater(() -> MainWindowController.PT2LabelAlternative.setTextFill(Color.WHITE));

			this.set_passedCustomsTerminal(_passedCustomsTerminal);
			this.setRowOnMap(rowOnMap - 1);
			this.setColumnOnMap(columnOnMap - 1);
		}

		// From PT1
		if (columnOnMap == 0) {
			MainWindowController.map[rowOnMap - 1][columnOnMap] = this;
			MainWindowController.map[rowOnMap][columnOnMap] = MainWindowController.PT1Object;
			Platform.runLater(() -> MainWindowController.PT1LabelAlternative.setText("PT1"));
			Platform.runLater(() -> MainWindowController.PT1LabelAlternative.setTextFill(Color.WHITE));

			this.set_passedCustomsTerminal(_passedCustomsTerminal);
			this.setRowOnMap(rowOnMap - 1);
		}

		System.out.println("Vozilo " + this.get_sign() + " se nalazi na poziciji " + this.getRowOnMap() + " "
				+ this.getColumnOnMap() + " a na mapi se nalazi "
				+ MainWindowController.map[this.getRowOnMap()][this.getColumnOnMap()]);

	}

	private synchronized boolean isFreeCTK(int rowOnMap, int columnOnMap) {
		System.out.println("isFreeCTK: rowOnMap and columnOnMap is " + rowOnMap + " " + columnOnMap);
		if (MainWindowController.map[rowOnMap-1][columnOnMap] instanceof CustomsTruckTerminal) {
			if(((CustomsTruckTerminal)MainWindowController.map[rowOnMap-1][columnOnMap]).is_isItOpenc()) {
				return true;
			}
		}
		return false;
	}

	private synchronized void freeCTK(int rowOnMap, int columnOnMap) {
		MainWindowController.map[rowOnMap][columnOnMap] = MainWindowController.CTKObject;
		Platform.runLater(() -> MainWindowController.CKLabelAlternative.setText("CTK"));
		Platform.runLater(() -> MainWindowController.CKLabelAlternative.setTextFill(Color.WHITE));

	}

	private synchronized void takeCTKOnMap(int rowOnMap, int columnOnMap) {
		MainWindowController.map[rowOnMap - 1][columnOnMap] = this;
		MainWindowController.map[rowOnMap][columnOnMap] = MainWindowController.PTKObject;
		Platform.runLater(() -> MainWindowController.PTKLabelAlternative.setText("PTK"));
		Platform.runLater(() -> MainWindowController.PTKLabelAlternative.setTextFill(Color.WHITE));

		this.set_passedCustomsTerminal(_passedCustomsTerminal);
		this.setRowOnMap(rowOnMap - 1);
		this.setColumnOnMap(columnOnMap);
	}

	/** Na policijskim terminalima */
	private void checkPassengersDocuments(ArrayList<Person> persons, Person driver) {
		int index = 0;
		// stop driving if document from driver is illegal.
		if (!checkIsDriverDocumentIsCorrect(driver)) {
			if (this.rowOnMap == 1 && this.columnOnMap == 0) {
				freePoliceTerminal(this.rowOnMap, this.columnOnMap);
			}

			if (this.rowOnMap == 1 && this.columnOnMap == 1) {
				freePoliceTerminal(this.rowOnMap, this.columnOnMap);

			}
			this.isRunning = false;
			System.out
					.println("Vozilo " + this.get_sign() + " je izbaceno iz voznje jer vozac nema ispravne dokumente!");
		}

		while (index < (persons.size() - 1)) {
			if (!checkIsPassengerDocumentIsCorrect(index)) {
				System.out.println("Neispravni dokumenti od putnika u vozilu " + this.get_sign());
				CustomFileHandler.writeIntoIncidentsBinaryFile(CustomFileHandler.binaryFileName, "Person "
						+ persons.get(index).get_id() + " at vehicle " + this.get_sign() + " have illegal document\n");
				this.get_passengers().set(index, null);
			}
			index++;
		}
	}

	private boolean checkIsPassengerDocumentIsCorrect(int index) {
		System.out.println("Pregledaju se dokumenti od putnika!....");
		if (!this.get_passengers().get(index).isIllegalDocument()) {
			return true;
		}
		return false;
	}

	/** Na carinskim terminalima */
	private void checkPassengersSuitcase(ArrayList<Person> persons, Person driver) {
		int index = 0;
		System.out.println("Pregledaju se koferi od putnika!....");
		while (index < (persons.size())) {
			if (persons.get(index) != null) {
				if (checkIsPassengerSuitcaseHaveIllegalThings(index)) {
					System.out.println("Neispravni koferi od putnika u busu " + this.get_sign());
					CustomFileHandler.writeIntoIncidentsFile(CustomFileHandler.fileName, "Person "
							+ persons.get(index).get_id() + " at bus " + this.get_sign() + " have illegal suitcase\n",
							null);
					this.get_passengers().set(index, null);
				}
			}

			index++;
		}

	}

	private boolean checkIsDriverDocumentIsCorrect(Person driver) {
		System.out.println("Pregledaju se dokumenti od vozaca!....");

		if (!driver.isIllegalDocument()) {
			return true;
		}
		return false;

	}

	private boolean checkIsPassengerSuitcaseHaveIllegalThings(int index) {

		if (this.get_passengers().get(index).get_suitcase().is_illegalThings()) {
			return true;
		}
		return false;
	}

	private void checkTruckDocumentation() {
		int generatedDocumentation = MainWindowController.generateRealDocumentation();
		System.out.println("Pregledava se dokumentacija od kamiona!....");
		// ako je 1 znaci da je prekoracio dozvoljeni teret
		if (generatedDocumentation == 1) {
			CustomFileHandler.writeIntoIncidentsFile(CustomFileHandler.fileName,
					"Truck " + this.get_id() + " is overweight! \n", null);
			System.out.println("Kamion je pretovaren u odnosu na stvarnu dokumentaciju!....");
			freePoliceTruckTerminal(rowOnMap, columnOnMap);
			this.isRunning = false;
		}
	}

	private void setSignOnLabel(int rowOnMap, int columnOnMap) {

		if (rowOnMap == 0 && columnOnMap == 0) {
			Platform.runLater(() -> MainWindowController.CTLabelAlternative.setText(this.get_sign()));
			if (this.typeOfVehicle() == 1) {
				Platform.runLater(() -> MainWindowController.CTLabelAlternative.setTextFill(Color.GOLD));
			} else if (this.typeOfVehicle() == 2) {
				Platform.runLater(() -> MainWindowController.CTLabelAlternative.setTextFill(Color.RED));
			} else {

				Platform.runLater(() -> MainWindowController.CTLabelAlternative.setTextFill(Color.LIGHTGREEN));

			}

		} else if (rowOnMap == 0 && columnOnMap == 2) {
			Platform.runLater(() -> MainWindowController.CKLabelAlternative.setText(this.get_sign()));
			if (this.typeOfVehicle() == 1) {
				Platform.runLater(() -> MainWindowController.CKLabelAlternative.setTextFill(Color.GOLD));
			} else if (this.typeOfVehicle() == 2) {
				Platform.runLater(() -> MainWindowController.CKLabelAlternative.setTextFill(Color.RED));
			} else {

				Platform.runLater(() -> MainWindowController.CKLabelAlternative.setTextFill(Color.LIGHTGREEN));

			}
		} else if (rowOnMap == 1 && columnOnMap == 0) {
			Platform.runLater(() -> MainWindowController.PT1LabelAlternative.setText(this.get_sign()));
			if (this.typeOfVehicle() == 1) {
				Platform.runLater(() -> MainWindowController.PT1LabelAlternative.setTextFill(Color.GOLD));
			} else if (this.typeOfVehicle() == 2) {
				Platform.runLater(() -> MainWindowController.PT1LabelAlternative.setTextFill(Color.RED));
			} else {

				Platform.runLater(() -> MainWindowController.PT1LabelAlternative.setTextFill(Color.LIGHTGREEN));

			}
		} else if (rowOnMap == 1 && columnOnMap == 1) {
			Platform.runLater(() -> MainWindowController.PT2LabelAlternative.setText(this.get_sign()));
			if (this.typeOfVehicle() == 1) {
				Platform.runLater(() -> MainWindowController.PT2LabelAlternative.setTextFill(Color.GOLD));
			} else if (this.typeOfVehicle() == 2) {
				Platform.runLater(() -> MainWindowController.PT2LabelAlternative.setTextFill(Color.RED));
			} else {

				Platform.runLater(() -> MainWindowController.PT2LabelAlternative.setTextFill(Color.LIGHTGREEN));

			}
		} else if (rowOnMap == 1 && columnOnMap == 2) {
			Platform.runLater(() -> MainWindowController.PTKLabelAlternative.setText(this.get_sign()));
			if (this.typeOfVehicle() == 1) {
				Platform.runLater(() -> MainWindowController.PTKLabelAlternative.setTextFill(Color.GOLD));
			} else if (this.typeOfVehicle() == 2) {
				Platform.runLater(() -> MainWindowController.PTKLabelAlternative.setTextFill(Color.RED));
			} else {

				Platform.runLater(() -> MainWindowController.PTKLabelAlternative.setTextFill(Color.LIGHTGREEN));

			}
		} else if (rowOnMap == 2 && columnOnMap == 1) {
			Platform.runLater(() -> MainWindowController.Queue1LabelAlternative.setText(this.get_sign()));
			if (this.typeOfVehicle() == 1) {
				Platform.runLater(() -> MainWindowController.Queue1LabelAlternative.setTextFill(Color.GOLD));
			} else if (this.typeOfVehicle() == 2) {
				Platform.runLater(() -> MainWindowController.Queue1LabelAlternative.setTextFill(Color.RED));
			} else {

				Platform.runLater(() -> MainWindowController.Queue1LabelAlternative.setTextFill(Color.LIGHTGREEN));

			}
		} else if (rowOnMap == 3 && columnOnMap == 1) {
			Platform.runLater(() -> MainWindowController.Queue2LabelAlternative.setText(this.get_sign()));
			if (this.typeOfVehicle() == 1) {
				Platform.runLater(() -> MainWindowController.Queue2LabelAlternative.setTextFill(Color.GOLD));
			} else if (this.typeOfVehicle() == 2) {
				Platform.runLater(() -> MainWindowController.Queue2LabelAlternative.setTextFill(Color.RED));
			} else {

				Platform.runLater(() -> MainWindowController.Queue2LabelAlternative.setTextFill(Color.LIGHTGREEN));

			}
		} else if (rowOnMap == 4 && columnOnMap == 1) {
			Platform.runLater(() -> MainWindowController.Queue3LabelAlternative.setText(this.get_sign()));
			if (this.typeOfVehicle() == 1) {
				Platform.runLater(() -> MainWindowController.Queue3LabelAlternative.setTextFill(Color.GOLD));
			} else if (this.typeOfVehicle() == 2) {
				Platform.runLater(() -> MainWindowController.Queue3LabelAlternative.setTextFill(Color.RED));
			} else {

				Platform.runLater(() -> MainWindowController.Queue3LabelAlternative.setTextFill(Color.LIGHTGREEN));

			}
		} else if (rowOnMap == 5 && columnOnMap == 1) {
			Platform.runLater(() -> MainWindowController.Queue4LabelAlternative.setText(this.get_sign()));
			if (this.typeOfVehicle() == 1) {
				Platform.runLater(() -> MainWindowController.Queue4LabelAlternative.setTextFill(Color.GOLD));
			} else if (this.typeOfVehicle() == 2) {
				Platform.runLater(() -> MainWindowController.Queue4LabelAlternative.setTextFill(Color.RED));
			} else {

				Platform.runLater(() -> MainWindowController.Queue4LabelAlternative.setTextFill(Color.LIGHTGREEN));

			}
		} else if (rowOnMap == 6 && columnOnMap == 1) {
			Platform.runLater(() -> MainWindowController.Queue5LabelAlternative.setText(this.get_sign()));
			if (this.typeOfVehicle() == 1) {
				Platform.runLater(() -> MainWindowController.Queue5LabelAlternative.setTextFill(Color.GOLD));
			} else if (this.typeOfVehicle() == 2) {
				Platform.runLater(() -> MainWindowController.Queue5LabelAlternative.setTextFill(Color.RED));
			} else {

				Platform.runLater(() -> MainWindowController.Queue5LabelAlternative.setTextFill(Color.LIGHTGREEN));

			}
		}

	}

	private void setCellColorAndLabel(int rowOnMap, int columnOnMap, Color color, String label) {
		if (rowOnMap == 0 && columnOnMap == 0) {
			Platform.runLater(() -> MainWindowController.CTLabelAlternative.setText(this.get_sign()));
			MainWindowController.CTLabelAlternative.getText();

		}
	}

	public String toRGBCode(Color color) {
		return String.format("#%02X%02X%02X", (int) (color.getRed() * 255), (int) (color.getGreen() * 255),
				(int) (color.getBlue() * 255));
	}

	@Override
	public synchronized int typeOfVehicle() {
		return 0;
	}

	@Override
	public int compareTo(Vehicle vehicleInRow) {
		int vehiclePositionInRow = ((Vehicle) vehicleInRow).getRowOnMap();

		return this.rowOnMap - vehiclePositionInRow;
	}

	public void resumeFromPause() {
		synchronized (pauseLock) {
			_pauseDriving = false;
			pauseLock.notifyAll();
		}
	}

}
