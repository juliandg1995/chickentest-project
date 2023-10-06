package com.retooling.chickentestbackend.exceptions.farm;

public class InternalErrorException extends RuntimeException {
	public InternalErrorException(String message) {
		super(message);
	}
}
