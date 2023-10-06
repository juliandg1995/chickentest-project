package com.retooling.chickentestbackend.exceptions.farm;

public class EggNotFoundException extends RuntimeException {
    public EggNotFoundException(Long eggId) {
        super("Egg with ID: " + eggId + " not found");
    }

}
