package com.retooling.chickentestbackend.exceptions.farm;

public class CannotCreateFarmException extends Exception {
	public CannotCreateFarmException() {
		super("Failed to create Farm");
	}
}
