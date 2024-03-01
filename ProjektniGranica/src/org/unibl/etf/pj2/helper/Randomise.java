package org.unibl.etf.pj2.helper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Randomise {
	
	private Random randomise = new Random();
	
	private static int countRandomNumber(int minRange, int maxRange) {
		int randomNumber = 0;
		return randomNumber = (int)Math.floor(Math.random()*(maxRange-minRange+1)+minRange);
	}
	
	/** For 50% of trucks documentation is needed.
	 *  @return randomDocumentationGenerator when it's 0 documentation is not required
	 * 										 when it's 1 truck need to generate documentation
	 */
	public static int randomiseDocumentationForTruck() {
		int randomDocumentationGenerator = countRandomNumber(0,1);
		return randomDocumentationGenerator;
	}
	
	/** 70% of passengers have suitcase. 
	 *  @return randomNumber when it's between [1,7] passenger have suitcase 
	 *  					 when it's between [8,10] doesn't.
	 * */
	public static int randomiseSuitcaseForPerson() {
		int min = 1; 
		int max = 10;
		int randomNumber = (int)Math.floor(Math.random()*(max-min+1)+min);
		return randomNumber;	
	}
	
	/** 10% of passengers suitcase have illegal thing. 
	 * 	@return randomNumber when it's 1 passenger have illegal suitcase 
	 *  					 in other cases his suitcase is legal. 
	 */
	public static int randomiseIllegalThingForSuitcaseOfPerson() {
		int min = 1; 
		int max = 10;
		//uzmi od 1 do 10 (od 10% do 100%)
		int randomNumber = (int)Math.floor(Math.random()*(max-min+1)+min);
		//ako je broj unutar 70% udji u if s
		if(randomNumber > 1 && randomNumber <=7) {
			max = 7;
			//u 30% slucajeva ima nelegalne stvari
			//znaci ako je 1 ili 2 ima ilegalne stvari
			int randomNumberForSuitcase = (int)Math.floor(Math.random()*(max-min+1)+min);
			randomNumber = randomNumberForSuitcase;
		}
		return randomNumber;
	}
	
	/** 3% of passengers have illegal documents. 
	 * 	@return randomNumber when it's between [1,3] passenger have illegal documents 
	 *  					 in other cases his documents are correct. 
	 */
	public static int randomisePassengerDocuments() {
		int min = 1;
		int max = 100;
		int randomNumber = (int)Math.floor(Math.random()* (max-min+1)+min);
		return randomNumber;
	}
	
	public static int randomiseNumberOfPassengerForCar() {
		int min = 1; 
		int max = 5;
		int randomNumber = (int)Math.floor(Math.random()*(max-min+1)+min);
		return randomNumber;
	}
	
	public static int randomiseNumberOfPassengerForBus() {
		int min = 1; 
		int max = 52;
		int randomNumber = (int)Math.floor(Math.random()*(max-min+1)+min);
		return randomNumber;
	}
	
	public static int randomiseNumberOfPassengerForTruck() {
		int min = 1; 
		int max = 3;
		int randomNumber = (int)Math.floor(Math.random()*(max-min+1)+min);
		return randomNumber;
		
	}
	
	public static int[] randomisePositionInQueue() {
 	    int min = 2;
 	    int max = 52;
 	    int n = 50;
 	    
 	    ArrayList<Integer> allNumbers = new ArrayList<>();

 	    for (int i = min; i <= max; i++) {
 	        allNumbers.add(i);
 	    }
 	    

 	    Collections.shuffle(allNumbers, new Random());
 	    

 	    ArrayList<Integer> selectedNumbers = new ArrayList<>(allNumbers.subList(0, n));

 	    int[] randomNumbers = new int[n];
 	    for (int i = 0; i < n; i++) {
 	        randomNumbers[i] = selectedNumbers.get(i);
 	    }
 	    
 	    return randomNumbers;
 	}
	
	

}
