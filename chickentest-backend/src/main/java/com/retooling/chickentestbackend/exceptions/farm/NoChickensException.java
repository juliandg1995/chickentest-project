package com.retooling.chickentestbackend.exceptions.farm;

public class NoChickensException extends Exception {
    public NoChickensException() {
        super("Farm has no chickens");
    }
}
