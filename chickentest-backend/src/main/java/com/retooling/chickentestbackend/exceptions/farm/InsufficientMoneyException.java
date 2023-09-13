package com.retooling.chickentestbackend.exceptions.farm;

public class InsufficientMoneyException extends Exception {
    public InsufficientMoneyException() {
        super("Insufficient founds to buy products");
    }
}
