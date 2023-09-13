package com.retooling.chickentestbackend.exceptions.farm;

public class NoEggsException extends Exception {
    public NoEggsException() {
        super("Farm has no eggs");
    }
}
