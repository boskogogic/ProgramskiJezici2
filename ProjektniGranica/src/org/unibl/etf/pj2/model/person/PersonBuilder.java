package org.unibl.etf.pj2.model.person;

import java.util.ArrayList;
import java.util.List;

import org.unibl.etf.pj2.controller.MainWindowController;
import org.unibl.etf.pj2.helper.Constants;
import org.unibl.etf.pj2.helper.Randomise;

public class PersonBuilder {
	
	public PersonBuilder() { }
	
	public ArrayList<Person> build(int numberOfPersons){
		int numberOfPersonsAndDriver = numberOfPersons + 1;
		ArrayList<Person> persons = new ArrayList<>(numberOfPersonsAndDriver);
		int index = 0;
		int randomGenerator = 0;
		int randomIsDocumentIllegal = isDocumentsIllegal(numberOfPersonsAndDriver);
		System.out.println("Broj ilegalnih dokumenata je : " + randomIsDocumentIllegal);
		
		while(index < numberOfPersonsAndDriver) {
			
			//3% of persons has illegal documents -> while loop to set illegal document
			while(index < randomIsDocumentIllegal) {
					//nisu bitni koferi -> Ne bi trebali stici na pregledavanje kofera
					Suitcase s = new Suitcase(false);
					Person p = new Person(true, s);
//					ne udje nikad System.out.println("Osoba " + p.get_id() + "ima dokumente neispravne");
					
					persons.add(index, p);
					index++;
				}
				
			randomGenerator = Randomise.randomiseIllegalThingForSuitcaseOfPerson();
			if(randomGenerator == 1 || randomGenerator == 2) {
				Suitcase s = new Suitcase(true);
				Person p = new Person(false, s);
				System.out.println("Broj ilegalnih kofera : " + randomGenerator + " " + s.is_illegalThings());
				persons.add(index, p);
			}
			else {
				Suitcase s = new Suitcase(false);
				Person p = new Person(false, s);
				
				persons.add(index, p);
			}
			index++;
			
		}
		return persons;

	}
	
	public Person buildDriver() {
		return null;
		
	}
	
	private int isDocumentsIllegal(int numberOfPersonsAndDriver) {
		double precentOfNumber = numberOfPersonsAndDriver * 0.03;
		System.out.println("Broj persona " + numberOfPersonsAndDriver + " 3% je " + precentOfNumber);
		int randomDocumentGenerator = Math.round((float)precentOfNumber);

		return randomDocumentGenerator;
	}

}
