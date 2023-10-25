package com.retooling.chickentestbackend.exceptions.farm;

public class IterationException extends Exception {
    public IterationException(String operation) {
        super("Error when iterating the following operation: " + operation);
    }
}
