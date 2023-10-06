package com.retooling.chickentestbackend.exceptions.farm;

public class ChickenNotFoundException extends RuntimeException {
    public ChickenNotFoundException(Long chickenId) {
        super("Chicken with ID: " + chickenId + " not found");
    }
}
