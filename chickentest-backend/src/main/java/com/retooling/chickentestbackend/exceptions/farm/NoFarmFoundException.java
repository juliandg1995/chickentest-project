package com.retooling.chickentestbackend.exceptions.farm;

public class NoFarmFoundException extends Exception {
	
    public NoFarmFoundException(String searchCriteria) {
        super("No farm found with provided " + searchCriteria);
    }

}
