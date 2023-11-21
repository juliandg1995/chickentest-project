package com.retooling.chickentestbackend.exceptions.farm;

@SuppressWarnings("serial")
public class NegativeValuesException extends Exception {
	
	public NegativeValuesException() {
		super("Check input parameters: Negative values not allowed.");
	}

}
