package org.unibl.etf.pj2.controller;

import javafx.event.ActionEvent;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.unibl.etf.pj2.file.CustomFileHandler;
import org.unibl.etf.pj2.file.FileWatchCallable;
import org.unibl.etf.pj2.helper.Constants;
import org.unibl.etf.pj2.helper.Randomise;
import org.unibl.etf.pj2.model.border.CustomsTerminal;
import org.unibl.etf.pj2.model.border.CustomsTruckTerminal;
import org.unibl.etf.pj2.model.border.PoliceTerminal;
import org.unibl.etf.pj2.model.border.PoliceTruckTerminal;
import org.unibl.etf.pj2.model.person.Person;
import org.unibl.etf.pj2.model.person.PersonBuilder;
import org.unibl.etf.pj2.model.vehicle.Bus;
import org.unibl.etf.pj2.model.vehicle.Car;
import org.unibl.etf.pj2.model.vehicle.Truck;
import org.unibl.etf.pj2.model.vehicle.Vehicle;
import org.unibl.etf.pj2.service.QueueService;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

public class MainWindowController extends Application {
	
	private Stage stage;
	private Stage primaryStage;
	private Scene scene;
	private Parent root;
	

	public static int SLEEP_TIME =1000;
	
	
/*	@FXML
	public static Pane PT1;
	
	@FXML 
	public static Pane PT2;
	
	@FXML 
	public static Pane PTK;
	
	@FXML 
	public static Pane CT = new Pane();
	

	@FXML
	public static TextField CTTextField = new TextField();

	@FXML 
	public static Pane CTK;
	
	@FXML
	public static Pane Queue1;
	
	@FXML 
	public static Pane Queue2;
	
	@FXML 
	public static Pane Queue3;
	
	@FXML 
	public static Pane Queue4;
	
	@FXML 
	public static Pane Queue5;*/
	
	@FXML
	private Label CTLabel;
	
	@FXML
	private Label CKLabel;
	
	@FXML 
	private Label PT1Label;
	
	@FXML 
	private Label PT2Label;
	
	@FXML
	private Label PTKLabel;
	
	@FXML 
	private Label Queue1Label;
	
	@FXML 
	private Label Queue2Label;
	
	@FXML 
	private Label Queue3Label;
	
	@FXML 
	private Label Queue4Label;
	
	@FXML 
	private Label Queue5Label;
	
	@FXML 
	private Label timeLabel;
	
	public static Label timeLabelAlternative;
	
	 private Timeline timeline;
	 private int seconds = 0;
	 private boolean isRunning = false;
	 private boolean hasStarted = false;

	private Object lock = new Object();  // Objekt za sinkronizaciju
	
	public static Label CheckLabelAlternative;
	
	public static Label CTLabelAlternative;
	
	public static Label CKLabelAlternative;

	public static Label PT1LabelAlternative;

	public static Label PT2LabelAlternative;
	
	public static Label PTKLabelAlternative;
	
	public static Label Queue1LabelAlternative;
	
	public static Label Queue2LabelAlternative;
	
	public static Label Queue3LabelAlternative;
	
	public static Label Queue4LabelAlternative;
	
	public static Label Queue5LabelAlternative;
	
	@FXML 
	Button startSimulationButton;
	
	@FXML
	Button buttonVehicleInformation;
	
	@FXML
	Button buttonIncidents;
	
	@FXML 
	Button otherVehicles;
	
	/** 3 reda i 52 kolone -> 2 reda za carinske i policijske terminale, a 50 redova za vozila */
	public static Object[][] map = new Object[53][3];
	
	public static CustomsTerminal CT1Object = new CustomsTerminal(true);
	
	public static CustomsTruckTerminal CTKObject = new CustomsTruckTerminal(true);
	
	public static PoliceTerminal PT1Object = new PoliceTerminal(true);
	
	public static PoliceTerminal PT2Object = new PoliceTerminal(true);
	
	public static PoliceTruckTerminal PTKObject = new PoliceTruckTerminal(true);

	
	public static int NUMBER_OF_VEHICLES = 7;
	
	public static int PERSON_COUNTER = 0;
	
	public synchronized static void initializeTerminals() {
		map[0][0] = CT1Object;
		map[0][2] = CTKObject;
		map[1][0] = PT1Object;
		map[1][1] = PT2Object;
		map[1][2] = PTKObject;
	}
	
	/**
	 *
	 */
	@Override
	public synchronized void start(Stage primaryStage) throws Exception {
		try {
			
			Parent root = FXMLLoader.load(getClass().getResource("/MainWindow.fxml"));
			Scene scene = new Scene(root,622,450);
			scene.getStylesheets().add(getClass().getResource("/application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
			primaryStage.setTitle("Simulation");
			System.out.println("Koliko mapa ima clanova " + map.length);
			
    		//String file = CustomFileHandler.createSimulationFile();
    		//String binaryFile = CustomFileHandler.createBinarySimulationFile();
    		
    		//CustomFileHandler.writeIntoIncidentsFile(file, "Incidents occured", null);
    		//CustomFileHandler.writeIntoIncidentsFile(file, "Incidents occured 2", null);
    		//CustomFileHandler.writeIntoIncidentsFile(file, "Incidents occured 3", null);
    		
    	
    		TextArea otherVehicles = new TextArea();

    		QueueWindowController.setOtherVehiclesAlternative(otherVehicles);
    		//System.out.println("Tekst iz fajla je : " + CustomFileHandler.readIncidentsFromFile(file).get(0));
		
		} catch(Exception ex) {
            Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);

		}
	}

	
	public synchronized void startSimulation(ActionEvent event) throws InterruptedException, FileNotFoundException {
		if(Constants.START_SIMULATION.equals(startSimulationButton.getText())) {
			// Inicijalizacija Timeline-a
			ExecutorService executor = Executors.newCachedThreadPool();

			executor.submit(new FileWatchCallable());
			
			
            timeline = new Timeline(new KeyFrame(Duration.seconds(1), this::updateTime));
            timeline.setCycleCount(Timeline.INDEFINITE);
			startTimer();
			startSimulationButton.setText(Constants.STOP_SIMULATION);
			
			CTLabelAlternative = CTLabel;
			Platform.runLater(()-> CTLabelAlternative.setText("CT"));
			
			CKLabelAlternative = CKLabel;
    		Platform.runLater(()-> CKLabelAlternative.setText("CK"));

			PT1LabelAlternative = PT1Label;
    		Platform.runLater(()-> PT1LabelAlternative.setText("PT1"));

			PT2LabelAlternative = PT2Label;
    		Platform.runLater(()-> PT2LabelAlternative.setText("PT2"));

			PTKLabelAlternative = PTKLabel;
    		Platform.runLater(()-> PTKLabelAlternative.setText("PTK"));
 
    		/**	public Vehicle(int _id, int _capacity, 
    		 * String _sign, ArrayList<Person> _passengers, 
    		 * Person _driver, boolean _pauseDriving, 
    		 * boolean _passedPoliceTerminal, boolean _passedCustomsTerminal) {
    		 */
    		int[] randomCarCapacity = new int[35];
    		int k =0;
    		while(k<35) {
    			randomCarCapacity[k] = Randomise.randomiseNumberOfPassengerForCar();
    			k++;
    		}
    		
    		PersonBuilder builder = new PersonBuilder();
    		
			Car car1 =  new Car(1, randomCarCapacity[0], "C1",  builder.build(randomCarCapacity[0]), builder.buildDriver(), false, false,false);
			System.out.println("Driver is : " +car1.get_driver().toString());
			Car car2 = new Car(2,randomCarCapacity[1],"C2",builder.build(randomCarCapacity[1]),builder.buildDriver(),false,false,false);
			Car car3 = new Car(3,randomCarCapacity[2],"C3",builder.build(randomCarCapacity[2]),builder.buildDriver(),false,false,false);
			Car car4 = new Car(4,randomCarCapacity[3],"C4",builder.build(randomCarCapacity[3]),builder.buildDriver(),false,false,false);
			Car car5 = new Car(5,randomCarCapacity[4], "C5", builder.build(randomCarCapacity[4]),builder.buildDriver(), false, false,false);
			
			Car car6 =  new Car(6, randomCarCapacity[5], "C6", builder.build(randomCarCapacity[5]), builder.buildDriver(),false, false,false);
			Car car7 = new Car(7,randomCarCapacity[6],"C7",builder.build(randomCarCapacity[6]), builder.buildDriver(),false,false,false);
			Car car8 = new Car(8,randomCarCapacity[7],"C8",builder.build(randomCarCapacity[7]), builder.buildDriver(),false,false,false);
			Car car9 = new Car(9,randomCarCapacity[8],"C9",builder.build(randomCarCapacity[8]), builder.buildDriver(),false,false,false);
			Car car10 = new Car(10,randomCarCapacity[9], "C10", builder.build(randomCarCapacity[9]), builder.buildDriver(), false, false,false);
			
			Car car11 =  new Car(11, randomCarCapacity[10], "C11", builder.build(randomCarCapacity[10]), builder.buildDriver(),false, false,false);
			Car car12 = new Car(12,randomCarCapacity[11],"C12",builder.build(randomCarCapacity[11]), builder.buildDriver(),false,false,false);
			Car car13 = new Car(13,randomCarCapacity[12],"C13",builder.build(randomCarCapacity[12]), builder.buildDriver(),false,false,false);
			Car car14 = new Car(14,randomCarCapacity[13],"C14",builder.build(randomCarCapacity[13]), builder.buildDriver(),false,false,false);
			Car car15 = new Car(15,randomCarCapacity[14], "C15", builder.build(randomCarCapacity[14]), builder.buildDriver(), false, false,false);
			
			Car car16 =  new Car(16, randomCarCapacity[15], "C16", builder.build(randomCarCapacity[15]), builder.buildDriver(),false, false,false);
			Car car17 = new Car(17,randomCarCapacity[16],"C17",builder.build(randomCarCapacity[16]), builder.buildDriver(),false,false,false);
			Car car18 = new Car(18,randomCarCapacity[17],"C18",builder.build(randomCarCapacity[17]), builder.buildDriver(),false,false,false);
			Car car19 = new Car(19,randomCarCapacity[18],"C19",builder.build(randomCarCapacity[18]), builder.buildDriver(),false,false,false);
			Car car20 = new Car(20,randomCarCapacity[19], "C20", builder.build(randomCarCapacity[19]), builder.buildDriver(), false, false,false);
			
			Car car21 =  new Car(21, randomCarCapacity[20], "C21", builder.build(randomCarCapacity[20]), builder.buildDriver(),false, false,false);
			Car car22 = new Car(22,randomCarCapacity[21],"C22",builder.build(randomCarCapacity[21]), builder.buildDriver(),false,false,false);
			Car car23 = new Car(23,randomCarCapacity[22],"C23",builder.build(randomCarCapacity[22]), builder.buildDriver(),false,false,false);
			Car car24 = new Car(24,randomCarCapacity[23],"C24",builder.build(randomCarCapacity[23]), builder.buildDriver(),false,false,false);
			Car car25 = new Car(25,randomCarCapacity[24], "C25",builder.build(randomCarCapacity[24]), builder.buildDriver(), false, false,false);
		
			Car car26 =  new Car(26, randomCarCapacity[25], "C26", builder.build(randomCarCapacity[25]), builder.buildDriver(),false, false,false);
			Car car27 = new Car(27,randomCarCapacity[26],"C27",builder.build(randomCarCapacity[26]), builder.buildDriver(),false,false,false);
			Car car28 = new Car(28,randomCarCapacity[27],"C28",builder.build(randomCarCapacity[27]), builder.buildDriver(),false,false,false);
			Car car29 = new Car(29,randomCarCapacity[28],"C29",builder.build(randomCarCapacity[28]), builder.buildDriver(),false,false,false);
			Car car30 = new Car(30,randomCarCapacity[29], "C30", builder.build(randomCarCapacity[29]), builder.buildDriver(), false, false,false);
			
			Car car31 =  new Car(31, randomCarCapacity[30], "C31", builder.build(randomCarCapacity[30]), builder.buildDriver(),false, false,false);
			Car car32 = new Car(32,randomCarCapacity[31],"C32",builder.build(randomCarCapacity[31]), builder.buildDriver(),false,false,false);
			Car car33 = new Car(33,randomCarCapacity[32],"C33",builder.build(randomCarCapacity[32]), builder.buildDriver(),false,false,false);
			Car car34 = new Car(34,randomCarCapacity[33],"C34",builder.build(randomCarCapacity[33]), builder.buildDriver(),false,false,false);
			Car car35 = new Car(35,randomCarCapacity[34], "C35",builder.build(randomCarCapacity[34]), builder.buildDriver(), false, false,false);
		
			int[] randomBusCapacity = new int[5];
    		k =0;
    		while(k<5) {
    			randomBusCapacity[k] = Randomise.randomiseNumberOfPassengerForBus();
    			k++;
    		}
    		
			Bus bus1 = new Bus(36,randomBusCapacity[0], "B1", builder.build(randomBusCapacity[0]), builder.buildDriver(), false, false,false);
			Bus bus2 = new Bus(37,randomBusCapacity[1], "B2", builder.build(randomBusCapacity[1]), builder.buildDriver(), false, false,false);
			Bus bus3 = new Bus(38,randomBusCapacity[2], "B3", builder.build(randomBusCapacity[2]), builder.buildDriver(), false, false,false);
			Bus bus4 = new Bus(39,randomBusCapacity[3], "B4", builder.build(randomBusCapacity[3]), builder.buildDriver(), false, false,false);
			Bus bus5 = new Bus(40,randomBusCapacity[4], "B5", builder.build(randomBusCapacity[4]), builder.buildDriver(), false, false,false);

			int[] randomTruckCapacity = new int[10];
    		k =0;
    		while(k<10) {
    			randomTruckCapacity[k] = Randomise.randomiseNumberOfPassengerForTruck();
    			k++;
    		}
    		
			Truck truck1 = new Truck(41,randomTruckCapacity[0], "T1", builder.build(randomTruckCapacity[0]), builder.buildDriver(), false, false,false);
			Truck truck2 = new Truck(42,randomTruckCapacity[1], "T2", builder.build(randomTruckCapacity[1]), builder.buildDriver(), false, false,false);
			Truck truck3 = new Truck(43,randomTruckCapacity[2], "T3", builder.build(randomTruckCapacity[2]), builder.buildDriver(), false, false,false);
			Truck truck4 = new Truck(44,randomTruckCapacity[3], "T4", builder.build(randomTruckCapacity[3]), builder.buildDriver(), false, false,false);
			Truck truck5 = new Truck(45,randomTruckCapacity[4], "T5", builder.build(randomTruckCapacity[4]), builder.buildDriver(), false, false,false);
			Truck truck6 = new Truck(46,randomTruckCapacity[5], "T6", builder.build(randomTruckCapacity[5]), builder.buildDriver(), false, false,false);
			Truck truck7 = new Truck(47,randomTruckCapacity[6], "T7", builder.build(randomTruckCapacity[6]), builder.buildDriver(), false, false,false);
			Truck truck8 = new Truck(48,randomTruckCapacity[7], "T8", builder.build(randomTruckCapacity[7]), builder.buildDriver(), false, false,false);
			Truck truck9 = new Truck(49,randomTruckCapacity[8], "T9", builder.build(randomTruckCapacity[8]), builder.buildDriver(), false, false,false);
			Truck truck10 = new Truck(50,randomTruckCapacity[9], "T10", builder.build(randomTruckCapacity[9]), builder.buildDriver(), false, false,false);

			initializeTerminals();
			System.out.println("Sta je na PT1 : " + map[1][0]);
			
			Queue1LabelAlternative = Queue1Label;
			//Platform.runLater(()-> Queue1LabelAlternative.setText(car1.get_sign()));
			//Platform.runLater(()-> Queue1LabelAlternative.setTextFill(Color.GOLD));

			Queue2LabelAlternative = Queue2Label;
			//Platform.runLater(()-> Queue2LabelAlternative.setText(car2.get_sign()));
			//Platform.runLater(()-> Queue2LabelAlternative.setTextFill(Color.GOLD));
			Queue3LabelAlternative = Queue3Label;
			//Platform.runLater(()-> Queue3LabelAlternative.setText(truck1.get_sign()));
			//Platform.runLater(()-> Queue3LabelAlternative.setTextFill(Color.GOLD));
			Queue4LabelAlternative = Queue4Label;
			//Platform.runLater(()-> Queue4LabelAlternative.setText(bus1.get_sign()));
			//Platform.runLater(()-> Queue4LabelAlternative.setTextFill(Color.GOLD));
			Queue5LabelAlternative = Queue5Label;
			//Platform.runLater(()-> Queue5LabelAlternative.setText(car3.get_sign()));
			//Platform.runLater(()-> Queue5LabelAlternative.setTextFill(Color.GOLD));
			
			int[] randomPlacesOnMap = new int[50];
			randomPlacesOnMap = Randomise.randomisePositionInQueue();
			
			int j=0;
			while(j<50) {
				System.out.println("Random mjesta su : " +randomPlacesOnMap[j]);
				j++;
			}
			
			map[randomPlacesOnMap[0]][1] = car1;
			map[randomPlacesOnMap[1]][1] = car2;
			map[randomPlacesOnMap[2]][1] = car3;
			map[randomPlacesOnMap[3]][1] = car4;
			map[randomPlacesOnMap[4]][1] = car5;
			map[randomPlacesOnMap[5]][1] = car6;
			map[randomPlacesOnMap[6]][1] = car7;
			map[randomPlacesOnMap[7]][1] = car8;
			map[randomPlacesOnMap[8]][1] = car9;
			map[randomPlacesOnMap[9]][1] = car10;
			
			map[randomPlacesOnMap[10]][1] = car11;
			map[randomPlacesOnMap[11]][1] = car12;
			map[randomPlacesOnMap[12]][1] = car13;
			map[randomPlacesOnMap[13]][1] = car14;
			map[randomPlacesOnMap[14]][1] = car15;
			map[randomPlacesOnMap[15]][1] = car16;
			map[randomPlacesOnMap[16]][1] = car17;
			map[randomPlacesOnMap[17]][1] = car18;
			map[randomPlacesOnMap[18]][1] = car19;
			map[randomPlacesOnMap[19]][1] = car20;
			
			map[randomPlacesOnMap[20]][1] = car21;
			map[randomPlacesOnMap[21]][1] = car22;
			map[randomPlacesOnMap[22]][1] = car23;
			map[randomPlacesOnMap[23]][1] = car24;
			map[randomPlacesOnMap[24]][1] = car25;
			map[randomPlacesOnMap[25]][1] = car26;
			map[randomPlacesOnMap[26]][1] = car27;
			map[randomPlacesOnMap[27]][1] = car28;
			map[randomPlacesOnMap[28]][1] = car29;
			map[randomPlacesOnMap[29]][1] = car30;
			
			map[randomPlacesOnMap[30]][1] = car31;
			map[randomPlacesOnMap[31]][1] = car32;
			map[randomPlacesOnMap[32]][1] = car33;
			map[randomPlacesOnMap[33]][1] = car34;
			map[randomPlacesOnMap[34]][1] = car35;
			map[randomPlacesOnMap[35]][1] = truck1;
			map[randomPlacesOnMap[36]][1] = truck2;
			map[randomPlacesOnMap[37]][1] = truck3;
			map[randomPlacesOnMap[38]][1] = truck4;
			map[randomPlacesOnMap[39]][1] = truck5;
			
			map[randomPlacesOnMap[40]][1] = truck6;
			map[randomPlacesOnMap[41]][1] = truck7;
			map[randomPlacesOnMap[42]][1] = truck8;
			map[randomPlacesOnMap[43]][1] = truck9;
			map[randomPlacesOnMap[44]][1] = truck10;
			map[randomPlacesOnMap[45]][1] = bus1;
			map[randomPlacesOnMap[46]][1] = bus2;
			map[randomPlacesOnMap[47]][1] = bus3;
			map[randomPlacesOnMap[48]][1] = bus4;
			map[randomPlacesOnMap[49]][1] = bus5;
			
			
			//10
			car1.setColumnOnMap(1);
			car1.setRowOnMap(randomPlacesOnMap[0]);
			car2.setColumnOnMap(1);
			car2.setRowOnMap(randomPlacesOnMap[1]);
			car3.setColumnOnMap(1);
			car3.setRowOnMap(randomPlacesOnMap[2]);
			car4.setColumnOnMap(1);
			car4.setRowOnMap(randomPlacesOnMap[3]);
			car5.setColumnOnMap(1);
			car5.setRowOnMap(randomPlacesOnMap[4]);
			car6.setColumnOnMap(1);
			car6.setRowOnMap(randomPlacesOnMap[5]);
			car7.setColumnOnMap(1);
			car7.setRowOnMap(randomPlacesOnMap[6]);
			car8.setColumnOnMap(1);
			car8.setRowOnMap(randomPlacesOnMap[7]);
			car9.setColumnOnMap(1);
			car9.setRowOnMap(randomPlacesOnMap[8]);
			car10.setColumnOnMap(1);
			car10.setRowOnMap(randomPlacesOnMap[9]);

			car11.setColumnOnMap(1);
			car11.setRowOnMap(randomPlacesOnMap[10]);
			car12.setColumnOnMap(1);
			car12.setRowOnMap(randomPlacesOnMap[11]);
			car13.setColumnOnMap(1);
			car13.setRowOnMap(randomPlacesOnMap[12]);
			car14.setColumnOnMap(1);
			car14.setRowOnMap(randomPlacesOnMap[13]);
			car15.setColumnOnMap(1);
			car15.setRowOnMap(randomPlacesOnMap[14]);
			car16.setColumnOnMap(1);
			car16.setRowOnMap(randomPlacesOnMap[15]);
			car17.setColumnOnMap(1);
			car17.setRowOnMap(randomPlacesOnMap[16]);
			car18.setColumnOnMap(1);
			car18.setRowOnMap(randomPlacesOnMap[17]);
			car19.setColumnOnMap(1);
			car19.setRowOnMap(randomPlacesOnMap[18]);
			car20.setColumnOnMap(1);
			car20.setRowOnMap(randomPlacesOnMap[19]);
			
			car21.setColumnOnMap(1);
			car21.setRowOnMap(randomPlacesOnMap[20]);
			car22.setColumnOnMap(1);
			car22.setRowOnMap(randomPlacesOnMap[21]);
			car23.setColumnOnMap(1);
			car23.setRowOnMap(randomPlacesOnMap[22]);
			car24.setColumnOnMap(1);
			car24.setRowOnMap(randomPlacesOnMap[23]);
			car25.setColumnOnMap(1);
			car25.setRowOnMap(randomPlacesOnMap[24]);
			car26.setColumnOnMap(1);
			car26.setRowOnMap(randomPlacesOnMap[25]);
			car27.setColumnOnMap(1);
			car27.setRowOnMap(randomPlacesOnMap[26]);
			car28.setColumnOnMap(1);
			car28.setRowOnMap(randomPlacesOnMap[27]);
			car29.setColumnOnMap(1);
			car29.setRowOnMap(randomPlacesOnMap[28]);
			car30.setColumnOnMap(1);
			car30.setRowOnMap(randomPlacesOnMap[29]);
			
			car31.setColumnOnMap(1);
			car31.setRowOnMap(randomPlacesOnMap[30]);
			car32.setColumnOnMap(1);
			car32.setRowOnMap(randomPlacesOnMap[31]);
			car33.setColumnOnMap(1);
			car33.setRowOnMap(randomPlacesOnMap[32]);
			car34.setColumnOnMap(1);
			car34.setRowOnMap(randomPlacesOnMap[33]);
			car35.setColumnOnMap(1);
			car35.setRowOnMap(randomPlacesOnMap[34]);

			truck1.setColumnOnMap(1);
			truck1.setRowOnMap(randomPlacesOnMap[35]);
			truck2.setColumnOnMap(1);
			truck2.setRowOnMap(randomPlacesOnMap[36]);
			truck3.setColumnOnMap(1);
			truck3.setRowOnMap(randomPlacesOnMap[37]);
			truck4.setColumnOnMap(1);
			truck4.setRowOnMap(randomPlacesOnMap[38]);
			truck5.setColumnOnMap(1);
			truck5.setRowOnMap(randomPlacesOnMap[39]);
			truck6.setColumnOnMap(1);
			truck6.setRowOnMap(randomPlacesOnMap[40]);
			truck7.setColumnOnMap(1);
			truck7.setRowOnMap(randomPlacesOnMap[41]);
			truck8.setColumnOnMap(1);
			truck8.setRowOnMap(randomPlacesOnMap[42]);
			truck9.setColumnOnMap(1);
			truck9.setRowOnMap(randomPlacesOnMap[43]);
			truck10.setColumnOnMap(1);
			truck10.setRowOnMap(randomPlacesOnMap[44]);
			
			bus1.setColumnOnMap(1);
			bus1.setRowOnMap(randomPlacesOnMap[45]);
			bus2.setColumnOnMap(1);
			bus2.setRowOnMap(randomPlacesOnMap[46]);
			bus3.setColumnOnMap(1);
			bus3.setRowOnMap(randomPlacesOnMap[47]);
			bus4.setColumnOnMap(1);
			bus4.setRowOnMap(randomPlacesOnMap[48]);
			bus5.setColumnOnMap(1);
			bus5.setRowOnMap(randomPlacesOnMap[49]);
		
			//QueueService qs=new QueueService();
			QueueService.initialize();
			QueueService.addVehicle(car1,0, randomPlacesOnMap[0]);
			QueueService.addVehicle(car2,1, randomPlacesOnMap[1]);
			QueueService.addVehicle(car3,2, randomPlacesOnMap[2]);
			QueueService.addVehicle(car4,3, randomPlacesOnMap[3]);
			QueueService.addVehicle(car5,4, randomPlacesOnMap[4]);
			QueueService.addVehicle(car6,5, randomPlacesOnMap[5]);
			QueueService.addVehicle(car7,6, randomPlacesOnMap[6]);
			QueueService.addVehicle(car8,7, randomPlacesOnMap[7]);
			QueueService.addVehicle(car9,8, randomPlacesOnMap[8]);
			QueueService.addVehicle(car10,9, randomPlacesOnMap[9]);
			
			QueueService.addVehicle(car11,10, randomPlacesOnMap[10]);
			QueueService.addVehicle(car12,11, randomPlacesOnMap[11]);
			QueueService.addVehicle(car13,12, randomPlacesOnMap[12]);
			QueueService.addVehicle(car14,13, randomPlacesOnMap[13]);
			QueueService.addVehicle(car15,14, randomPlacesOnMap[14]);
			QueueService.addVehicle(car16,15, randomPlacesOnMap[15]);
			QueueService.addVehicle(car17,16, randomPlacesOnMap[16]);
			QueueService.addVehicle(car18,17, randomPlacesOnMap[17]);
			QueueService.addVehicle(car19,18, randomPlacesOnMap[18]);
			QueueService.addVehicle(car20,19, randomPlacesOnMap[19]);
			
			QueueService.addVehicle(car21,20, randomPlacesOnMap[20]);
			QueueService.addVehicle(car22,21, randomPlacesOnMap[21]);
			QueueService.addVehicle(car23,22, randomPlacesOnMap[22]);
			QueueService.addVehicle(car24,23, randomPlacesOnMap[23]);
			QueueService.addVehicle(car25,24, randomPlacesOnMap[24]);
			QueueService.addVehicle(car26,25, randomPlacesOnMap[25]);
			QueueService.addVehicle(car27,26, randomPlacesOnMap[26]);
			QueueService.addVehicle(car28,27, randomPlacesOnMap[27]);
			QueueService.addVehicle(car29,28, randomPlacesOnMap[28]);
			QueueService.addVehicle(car30,29, randomPlacesOnMap[29]);
			
			QueueService.addVehicle(car31,30, randomPlacesOnMap[30]);
			QueueService.addVehicle(car32,31, randomPlacesOnMap[31]);
			QueueService.addVehicle(car33,32, randomPlacesOnMap[32]);
			QueueService.addVehicle(car34,33, randomPlacesOnMap[33]);
			QueueService.addVehicle(car35,34, randomPlacesOnMap[34]);
			QueueService.addVehicle(truck1,35, randomPlacesOnMap[35]);
			QueueService.addVehicle(truck2,36, randomPlacesOnMap[36]);
			QueueService.addVehicle(truck3,37, randomPlacesOnMap[37]);
			QueueService.addVehicle(truck4,38, randomPlacesOnMap[38]);
			QueueService.addVehicle(truck5,39, randomPlacesOnMap[39]);
			
			QueueService.addVehicle(truck6,40, randomPlacesOnMap[40]);
			QueueService.addVehicle(truck7,41, randomPlacesOnMap[41]);
			QueueService.addVehicle(truck8,42, randomPlacesOnMap[42]);
			QueueService.addVehicle(truck9,43, randomPlacesOnMap[43]);
			QueueService.addVehicle(truck10,44, randomPlacesOnMap[44]);
			QueueService.addVehicle(bus1,45, randomPlacesOnMap[45]);
			QueueService.addVehicle(bus2,46, randomPlacesOnMap[46]);
			QueueService.addVehicle(bus3,47, randomPlacesOnMap[47]);
			QueueService.addVehicle(bus4,48, randomPlacesOnMap[48]);
			QueueService.addVehicle(bus5,49, randomPlacesOnMap[49]);
			
			
			QueueService.startVehicle(0);
			
		
			
		}
		else if(Constants.STOP_SIMULATION.equals(startSimulationButton.getText())){
			startSimulationButton.setText(Constants.CONTINUE_SIMULATION);
			pauseTimer();
			QueueService.stopVehicleDriving();
			Vehicle._pauseDriving = true;
			
		}
		else{
			synchronized(Vehicle.pauseLock) {
				Vehicle.pauseLock.notifyAll();
				QueueService.continueVehicleDriving();
				Vehicle._pauseDriving = false;
				startSimulationButton.setText(Constants.STOP_SIMULATION);
			}
			continueTimer();
			
			

		}
	}

	

	public synchronized void onClick_Button_Vehicle_Information(ActionEvent event) {
		try {
			switchToSceneInformationAboutVehicle(event);
		}catch(IOException ex) {
            Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	private synchronized void switchToSceneInformationAboutVehicle(ActionEvent event) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("/InformationAboutVehicleWindow.fxml"));
		Node node1;
		//scene.getStylesheets().add(getClass().getResource("/application.css").toExternalForm());

		Stage stage = new Stage();
		stage.initModality(Modality.APPLICATION_MODAL);
		scene = new Scene(root);
		stage.setScene(scene);
		stage.setTitle("Information about vehicle window");
		stage.show();
		
	}

	public void onClick_Button_Incidents(ActionEvent event) {
		try {
			switchToSceneIncidents(event);
		}catch(IOException ex) {
            Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	private void switchToSceneIncidents(ActionEvent event) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("/IncidentWindow.fxml"));
		Node node1;
		//scene.getStylesheets().add(getClass().getResource("/application.css").toExternalForm());

		Stage stage = new Stage();
		stage.initModality(Modality.APPLICATION_MODAL);
		scene = new Scene(root);
		stage.setScene(scene);
		stage.setTitle("Incidents");
		stage.show();
		
	}

	public void onClick_Button_Other_Vehicles(ActionEvent event) {
		try {
			switchToSceneOtherVehicles(event);
		}catch(IOException ex) {
            Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	private void switchToSceneOtherVehicles(ActionEvent event) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("/QueueWindow.fxml"));
		Node node1;
		//scene.getStylesheets().add(getClass().getResource("/application.css").toExternalForm());

		Stage stage = new Stage();
		stage.initModality(Modality.APPLICATION_MODAL);
		scene = new Scene(root);
		stage.setScene(scene);
		stage.setTitle("Other vehicles");
		stage.show();		
	}

	/** Potrebno je da napravim niz auta, rasporedim ih i prikazujem na mapi i mijenjam stanja*/
	public static void main(String[] args) {
		launch(args);
		/**	public Vehicle(int _id, int _capacity, String _sign, ArrayList<Person> _passengers, Person _driver, boolean _pauseDriving, boolean _passedPoliceTerminal) {*/

		
	}

    private void controlTimer(ActionEvent event) {
        // Kontrola stopera (START/PAUSE/CONTINUE)
        synchronized (lock) {
            if (!hasStarted) {
                startTimer();
                hasStarted = true;
            } else {
                if (isRunning) {
                    pauseTimer();
                } else {
                    continueTimer();
                }
            }
        }
    }

    private void startTimer() {
    	//timeLabelAlternative = new Label();
    	timeLabelAlternative = timeLabel;
        // Pokretanje stopera
        synchronized (lock) {
            timeline.play();
            updateLabel();
            isRunning = true;
        }
    }

    private void pauseTimer() {
        // Zaustavljanje stopera
        synchronized (lock) {
            timeline.pause();
            isRunning = false;
        }
    }

    private void continueTimer() {
        // Nastavljanje stopera
        synchronized (lock) {
            timeline.play();
            isRunning = true;
        }
    }

    @FXML
    private void resetTimer(ActionEvent event) {
        // Resetiranje stopera
        synchronized (lock) {
            timeline.stop();
            seconds = 0;
            isRunning = false;
            hasStarted = false;
            updateLabel();
        }
    }

    private void updateTime(ActionEvent event) {
        // Ažuriranje vremena
        synchronized (lock) {
            seconds++;
            updateLabel();
        }
    }

    private void updateLabel() {
        // Postavljanje vremena na etiketi
        int minutes = seconds / 60;
        int remainingSeconds = seconds % 60;
        //Platform.runLater(()->timeLabelAlternative.setText(String.format("%02d:%02d", minutes, remainingSeconds)));
        timeLabelAlternative.setText(String.format("%02d:%02d", minutes, remainingSeconds));
    }
	
    public static int generateRealDocumentation() {
		Random randomGenerator = new Random();
		int randomNumber = randomGenerator.nextInt(2); //0 treba dokumentacija 1 ne treba
		if(randomNumber == 0) {
			int randomOverweight = Truck.isOverweight();
			if(randomOverweight == 1 || randomOverweight==2) {
				int overWeight = Truck.generateOverweightRealWeight();
				//demonstrativno postavim da mu je tezina veca od deklarisane
				Truck._realWeightInKilograms = overWeight + Truck._declarativeWeightInKilograms;
				return 1;
			}
		}
		else {
			Truck._realWeightInKilograms = 0;
			
		}
		return 0;
	}
	
}
