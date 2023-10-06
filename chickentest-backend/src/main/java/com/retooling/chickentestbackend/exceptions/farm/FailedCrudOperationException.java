package com.retooling.chickentestbackend.exceptions.farm;

public class FailedCrudOperationException extends Exception {

    public FailedCrudOperationException(String message) {
        super("Error while performing CRUD operation: " + message);
    }
	
}
