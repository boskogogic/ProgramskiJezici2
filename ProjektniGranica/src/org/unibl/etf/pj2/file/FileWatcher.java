package org.unibl.etf.pj2.file;

import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.unibl.etf.pj2.controller.MainWindowController;
import org.unibl.etf.pj2.helper.Constants;
import org.unibl.etf.pj2.model.vehicle.Vehicle;
import org.unibl.etf.pj2.service.QueueService;

import javafx.application.Platform;

public class FileWatcher {
	
	/**
	 * LEGEND FOR FILEWATCH.TXT
	 * true; for PT1
	 * true; for PT2
	 * true; for PTK 
	 * true; for CT1
	 * true; for CTK
	 */
	public void watchFolder() {
		try{
			final WatchService WATCH_SERVICE = FileSystems.getDefault().newWatchService();
			Path FILE = Paths.get("C:\\Users\\bgogi\\eclipse-workspace\\ProjektniGranica\\watcher");
			WatchKey watchKey = FILE.register(WATCH_SERVICE, 
					StandardWatchEventKinds.ENTRY_MODIFY);
			
			while (true) {
				for (WatchEvent<?> event : watchKey.pollEvents()) {

					// STEP5: Get file name from even context
					WatchEvent<Path> pathEvent = (WatchEvent<Path>) event;

					Path fileName = pathEvent.context();

					// STEP6: Check type of event.
					WatchEvent.Kind<?> kind = event.kind();

					if (kind == StandardWatchEventKinds.ENTRY_MODIFY) {
						List<String> changes = new ArrayList<String>();
						changes = CustomFileHandler.readIncidentsFromFile("C:\\Users\\bgogi\\eclipse-workspace\\ProjektniGranica\\watcher\\fileWatch2.txt");
						//changes = Files.readAllLines(FILE);
						int indexOfChanges = 1;
						for(String change : changes) {
							System.out.println("UNUTAR FILE WATCHER change je " + change + " broj index" + indexOfChanges);
							String[] line = change.split(";");
							
							for(int i =0 ; i< line.length; i++) {
								System.out.println("What is inside " + i + " line : " + line[i]);

							}
	
								
								if(!Constants.OPENED_TERMINAL.equals(line[0]) && indexOfChanges==1) {
									System.out.println("Uhvacena promjena indexOfChanges 1 unutar watchera !");
									Vehicle._pauseDriving = true;
									System.out.println("Change inside file watcher PT1 closed");
									Platform.runLater(()-> MainWindowController.map[1][0] = "CLOSED");
									System.out.println("What is on CT1: " + MainWindowController.map[0][0]);
									MainWindowController.PT1Object.set_isItOpenc(false);
									indexOfChanges = 1;
									//QueueService.stopVehicleDriving();		
								}
								else if(!Constants.OPENED_TERMINAL.equals(line[0]) && indexOfChanges==2) {
									System.out.println("Uhvacena promjena indexOfChanges 2 unutar watchera !");
									Vehicle._pauseDriving = true;
									System.out.println("Change inside file watcher PT2 closed");
									Platform.runLater(()-> MainWindowController.map[1][1] = "CLOSED");
									System.out.println("What is on CT1: " + MainWindowController.map[0][0]);
									MainWindowController.PT2Object.set_isItOpenc(false);
									indexOfChanges = 2;
								}else if(!Constants.OPENED_TERMINAL.equals(line[0])&& indexOfChanges==3) {
									System.out.println("Uhvacena promjena indexOfChanges 3 unutar watchera !");
									Vehicle._pauseDriving = true;
									System.out.println("Change inside file watcher PTK closed");
									Platform.runLater(()-> MainWindowController.map[1][2] = "CLOSED");
									System.out.println("What is on CT1: " + MainWindowController.map[0][0]);
									indexOfChanges = 3;
									MainWindowController.PTKObject.set_isItOpenc(false);
								}else if(!Constants.OPENED_TERMINAL.equals(line[0])&& indexOfChanges==4) {
									System.out.println("Uhvacena promjena indexOfChanges 4 unutar watchera !");
									Vehicle._pauseDriving = true;
									System.out.println("Change inside file watcher CT1 closed");
									Platform.runLater(()-> MainWindowController.map[0][0] = "CLOSED");
									System.out.println("What is on CT1: " + MainWindowController.map[0][0]);
									MainWindowController.CT1Object.set__isItOpenc(false);
									indexOfChanges = 4;
								}else if(!Constants.OPENED_TERMINAL.equals(line[0])&& indexOfChanges==5) {
									System.out.println("Uhvacena promjena indexOfChanges 5 unutar watchera !");
									Vehicle._pauseDriving = true;
									System.out.println("Change inside file watcher PT2 closed");
									Platform.runLater(()-> MainWindowController.map[0][2] = "CLOSED");
									System.out.println("What is on CT1: " + MainWindowController.map[0][0]);
									MainWindowController.CTKObject.set_isItOpenc(false);
									indexOfChanges = 5;
								}
								//ako je true a terminal nije otvoren
								else {
									if(indexOfChanges == 1 && !MainWindowController.PT1Object.is_isItOpenc() ) {
										System.out.println("Uhvacena promjena TRUE indexOfChanges 1 unutar watchera !");
										Vehicle._pauseDriving = true;
										System.out.println("Change inside file watcher PT1 closed");
										Platform.runLater(()-> MainWindowController.map[1][0] = MainWindowController.PT1Object);
										System.out.println("What is on CT1: " + MainWindowController.map[0][0]);
										MainWindowController.PT1Object.set_isItOpenc(true);
									}
									else if(indexOfChanges == 2 && !MainWindowController.PT2Object.is_isItOpenc()) {
										System.out.println("Uhvacena promjena TRUEindexOfChanges 1 unutar watchera !");
										Vehicle._pauseDriving = true;
										System.out.println("Change inside file watcher PT1 closed");
										Platform.runLater(()-> MainWindowController.map[1][1] = MainWindowController.PT2Object);
										System.out.println("What is on CT1: " + MainWindowController.map[0][0]);
										MainWindowController.PT2Object.set_isItOpenc(true);
									}
									else if(indexOfChanges == 3 && !MainWindowController.PTKObject.is_isItOpenc()) {
										System.out.println("Uhvacena promjena TRUEindexOfChanges 1 unutar watchera !");
										Vehicle._pauseDriving = true;
										System.out.println("Change inside file watcher PT1 closed");
										Platform.runLater(()-> MainWindowController.map[1][2] = MainWindowController.PTKObject);
										System.out.println("What is on CT1: " + MainWindowController.map[0][0]);
										MainWindowController.PTKObject.set_isItOpenc(true);
									}
									else if(indexOfChanges == 4 && !MainWindowController.CT1Object._isItOpenc()) {
										System.out.println("Uhvacena promjena TRUEindexOfChanges 1 unutar watchera !");
										Vehicle._pauseDriving = true;
										System.out.println("Change inside file watcher PT1 closed");
										Platform.runLater(()-> MainWindowController.map[0][0] = MainWindowController.CT1Object);
										System.out.println("What is on CT1: " + MainWindowController.map[0][0]);
										MainWindowController.CT1Object.set__isItOpenc(true);
									}
									else if(indexOfChanges == 5 && !MainWindowController.CTKObject.is_isItOpenc()) {
										System.out.println("Uhvacena promjena TRUEindexOfChanges 1 unutar watchera !");
										Vehicle._pauseDriving = true;
										System.out.println("Change inside file watcher PT1 closed");
										Platform.runLater(()-> MainWindowController.map[0][2] = MainWindowController.CTKObject);
										System.out.println("What is on CT1: " + MainWindowController.map[0][0]);
										MainWindowController.CTKObject.set_isItOpenc(true);
									}
								}
								
								
								System.out.println("Change inside file watcher closed");
								//Platform.runLater(()-> MainWindowController.map[1][0] = "CLOSED");
								System.out.println("What is on CT1: " + MainWindowController.map[0][0]);
								indexOfChanges++;
								Vehicle._pauseDriving = false;
							}

						}
					}
					boolean valid = watchKey.reset();
					if (!valid) {
						break;
					}
				}
				

			}catch(Exception ex) {
			Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}
