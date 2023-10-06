package com.retooling.chickentestbackend.exceptions.farm;

public class FarmNotFoundException extends RuntimeException{
    public FarmNotFoundException(Long farmId) {
        super("Farm with ID: " + farmId + " not found");
    }
}
