package com.retooling.chickentestbackend.dto;

public class EggRequestDTO {
	private Long farmId;
	private double sellPrice;
	
	public EggRequestDTO() {
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
}
