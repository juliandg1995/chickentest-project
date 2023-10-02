package com.retooling.chickentestbackend.dto;

public class ChickenRequestDTO {

	private Long farmId;
	private int age;
	private double sellPrice;

	public ChickenRequestDTO() {
	}

	public Long getFarmId() {
		return farmId;
	}

	public void setFarmId(Long farmId) {
		this.farmId = farmId;
	}

	public double getSellPrice() {
		return sellPrice;
	}

	public void setSellPrice(double sellPrice) {
		this.sellPrice = sellPrice;
	}
	
	public int getAge() {
		return this.age;
	}
	
	public void setAge(int age) {
		this.age = age;
	}

}
