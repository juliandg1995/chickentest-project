package com.retooling.chickentestbackend.dto;

public class FarmRequestDTO {
	
	private String name;
	private double money;
	
	public FarmRequestDTO() {
		
	}
	
	public FarmRequestDTO(String aName, double money) {
		name = aName;
		this.money = money;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getMoney() {
		return money;
	}
	public void setMoney(double money) {
		this.money = money;
	}

}
