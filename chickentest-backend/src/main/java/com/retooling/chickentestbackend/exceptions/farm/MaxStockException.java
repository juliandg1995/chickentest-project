package com.retooling.chickentestbackend.exceptions.farm;

@SuppressWarnings("serial")
public class MaxStockException extends Exception {

    public MaxStockException(String product) {
        super("Not enough " + product + " stock storage");
    }
	
}
