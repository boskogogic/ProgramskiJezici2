package org.unibl.etf.pj2.model.border;

import org.unibl.etf.pj2.model.border.interfaces.ITerminal;

public class CustomsTerminal implements ITerminal{
	
	private boolean _isItOpenc;

	public CustomsTerminal(boolean _isItOpenc) {
		super();
		this._isItOpenc = _isItOpenc;
	}

	public boolean _isItOpenc() {
		return _isItOpenc;
	}

	public void set__isItOpenc(boolean _isItOpenc) {
		this._isItOpenc = _isItOpenc;
	}

	@Override
	public int typeOfTerminal() {
		// TODO Auto-generated method stub
		return 0;
	}
}
