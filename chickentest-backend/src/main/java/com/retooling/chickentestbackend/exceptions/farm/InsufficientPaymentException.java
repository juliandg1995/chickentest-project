package com.retooling.chickentestbackend.exceptions.farm;

public class InsufficientPaymentException extends Exception {
    public InsufficientPaymentException() {
        super("Insufficient payment to buy indicated amount of products");
    }
}
