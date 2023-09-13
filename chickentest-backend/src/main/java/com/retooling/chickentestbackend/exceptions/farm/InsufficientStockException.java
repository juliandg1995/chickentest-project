package com.retooling.chickentestbackend.exceptions.farm;

public class InsufficientStockException extends Exception {
	
    public InsufficientStockException() {
        super("Insufficient product stock");
    }	
}
