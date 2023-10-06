package com.retooling.chickentestbackend.exceptions.farm;

public class FailedOperationException extends Exception {
    public FailedOperationException(String operation) {
        super("Error while trying to perform the following operation: " + operation);
    }
}
