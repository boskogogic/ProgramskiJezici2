package org.unibl.etf.pj2.model.person;

import org.unibl.etf.pj2.controller.MainWindowController;

public class Person {
	
	private int _id;
	
	private boolean _illegalDocument;
	
	private Suitcase _suitcase;

	public Person(boolean _illegalDocument, Suitcase _suitcase) {
		super();
		this._id = MainWindowController.PERSON_COUNTER++;
		this._illegalDocument = _illegalDocument;
		this._suitcase = _suitcase;
	}

	public boolean isIllegalDocument() {
		return _illegalDocument;
	}

	public void set_illegalDocument(boolean _illegalDocument) {
		this._illegalDocument = _illegalDocument;
	}

	public Suitcase get_suitcase() {
		return _suitcase;
	}

	public void set_suitcase(Suitcase _suitcase) {
		this._suitcase = _suitcase;
	}

	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}
	
	

}
