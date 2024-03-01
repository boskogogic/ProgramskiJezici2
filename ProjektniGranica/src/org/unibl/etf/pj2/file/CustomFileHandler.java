package org.unibl.etf.pj2.file;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CustomFileHandler {
	
	public static  final String fileName = createSimulationFile();
	
	public static  final String binaryFileName = createBinarySimulationFile();
	
	public static ObjectInputStream read;
	 
	public static FileInputStream reader;
	

	/** Provjereno u testiranju malih funkcija projektu - RADI */
	public static String createSimulationFile() {
		String fileName = null;
		try{
			SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy_hh_mm_ss");
			fileName = sdf.format(new Date()) +".txt";
			FileOutputStream simulationFile = new FileOutputStream(fileName);
			simulationFile.close();
		}catch(FileNotFoundException ex) {
           Logger.getLogger(CustomFileHandler.class.getName()).log(Level.SEVERE, null, ex);
		}catch(IOException ex) {
	       Logger.getLogger(CustomFileHandler.class.getName()).log(Level.SEVERE, null, ex);

		}
		
		//CustomFileHandler.fileName = "./incidents/" + fileName+".txt";
		return fileName;
	}
	
	public static String createBinarySimulationFile() {
		String fileName = null;
		try{
			SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy_hh_mm_ss");
			fileName = sdf.format(new Date())  + ".bin" ;
			FileOutputStream simulationFile = new FileOutputStream(fileName, true);
			reader = new FileInputStream(fileName);
			simulationFile.close();	 
		}catch(FileNotFoundException ex) {
           Logger.getLogger(CustomFileHandler.class.getName()).log(Level.SEVERE, null, ex);
		}catch(IOException ex) {
		       Logger.getLogger(CustomFileHandler.class.getName()).log(Level.SEVERE, null, ex);

		}
		
		//CustomFileHandler.binaryFileName = "./incidents/" + fileName + ".bin";
		return fileName;
	}
	
	public static String getFile() {
		return null;
		
	}

	/** Method for reading incidents, that happened, from file. */
	public static ArrayList<String> readIncidentsFromFile(String file) {
		//provjereno - cita kako treba 
		ArrayList<String> messages = new ArrayList<String>();
		//String file = "C:\\Users\\bgogi\\eclipse-workspace\\ProjektniGranica\\files\\EvidenceOfNontransition.txt";
		int counter = 0;
		try{
			BufferedReader in = new BufferedReader(new FileReader(file));
			String line;
			while((line = in.readLine()) != null) {
				messages.add(line);
			}
		}catch(FileNotFoundException ex) {
            Logger.getLogger(CustomFileHandler.class.getName()).log(Level.SEVERE, null, ex);
		}catch(IOException ex) {
            Logger.getLogger(CustomFileHandler.class.getName()).log(Level.SEVERE, null, ex);
		}
		
		System.out.println(messages);
		return messages;
	}
	
	public static ArrayList<String> readIncidentsFromBinaryFile(String file) {
		ArrayList<String> messages = new ArrayList<String>();
		//FileInputStream reader;
		int counter = 0;
		try{
			reader = new FileInputStream(file);
			read = new ObjectInputStream(reader);
			
			int index=0;
			String line = null;
			//radi i upise sve kako treba samo baci exception nakon zavrsenog citanja ?!
			while((line = (String) read.readObject()) != null) {
				messages.add(line);
				index++;
			}
		
			read.close();
			reader.close();
		
		}catch(FileNotFoundException ex) {
            Logger.getLogger(CustomFileHandler.class.getName()).log(Level.SEVERE, null, ex);
		}catch(IOException ex) {
            Logger.getLogger(CustomFileHandler.class.getName()).log(Level.SEVERE, null, ex);
		}catch(ClassNotFoundException ex) {
            Logger.getLogger(CustomFileHandler.class.getName()).log(Level.SEVERE, null, ex);

		}
		
		System.out.println(messages);
		return messages;
	}

	public static void writeIntoIncidentsFile(String file, String message, Exception e) {
		try{
			PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
			out.println(message);
			System.out.println("Upis: " + message);

			out.close();
		}catch(Exception ex) {
			Logger.getLogger(CustomFileHandler.class.getName()).log(Level.SEVERE, null, ex);

		}
	}
	
	public static void writeIntoIncidentsBinaryFile(String file, String message) {
		try{
			FileOutputStream fileWriter = new FileOutputStream(file, true);
			ObjectOutputStream write = new ObjectOutputStream(fileWriter);
			write.writeObject(message);
			write.close();
			fileWriter.close();
		}catch(Exception ex)
		{
			Logger.getLogger(CustomFileHandler.class.getName()).log(Level.SEVERE, null, ex);
		}
		
	}
	
	public static ArrayList<String> readFileWatch(String file) {
		//provjereno - cita kako treba 
		ArrayList<String> messages = new ArrayList<String>();
		int counter = 0;
		try{
			BufferedReader in = new BufferedReader(new FileReader(file));
			String line = "";
			while((line = in.readLine()) != null) {
				messages.add(line);
			}
		}catch(FileNotFoundException ex) {
            Logger.getLogger(CustomFileHandler.class.getName()).log(Level.SEVERE, null, ex);
		}catch(IOException ex) {
            Logger.getLogger(CustomFileHandler.class.getName()).log(Level.SEVERE, null, ex);
		}
		
		System.out.println(messages);
		return messages;
	}
}
