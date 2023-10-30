package com.retooling.chickentestbackend.exceptions.farm;

public class NoEggsException extends Exception {
    public NoEggsException() {
        super("No Eggs Found");
    }
}
