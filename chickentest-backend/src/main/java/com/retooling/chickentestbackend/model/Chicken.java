package com.retooling.chickentestbackend.model;

import jakarta.persistence.*;

import java.util.*;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "chickens")
public class Chicken extends Product {
	
    @Column
	private String name;
    
    @Column
    private int daysToEggsCountdown;
    
    @Column
    private int ageInDays;
    
    private static int daysToPutEggs = 5;
    
    private static int eggAmount = 1;
    
	/** Constructors **/
    
	public Chicken() {
		super();
	}
    
	public Chicken(double sellPrice,  int age, Farm farmOwner) {
		super(sellPrice, farmOwner);
		this.daysToEggsCountdown = daysToPutEggs;
		this.ageInDays = age;
	}
	
	/** Getters - Setters **/

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public static int getDaysToPutEggs() {
		return daysToPutEggs;
	}

	public int getDaysToEggsCountdown() {
		return daysToEggsCountdown;
	}

	public int getAge() {
		return ageInDays;
	}

	public void setAge(int age) {
		this.ageInDays = age;
	}
	
	@Override
	public void passDays(int days) {
		for (int i = 0 ; i < days; i++) {
			ageInDays++;
			this.daysToEggsCountdown--;
			if (daysToEggsCountdown == 0) {
				break;
			}
		}
	}
	
	public void resetDaysToEggsCountdown() {
		this.daysToEggsCountdown = daysToPutEggs;
	}
	
	public void hatchEggs(List<Egg> eggs) {
		for(Egg egg: eggs) {
			egg.hatch();
		}
	}
	
	@Override
	public boolean isDiscountMaterial() {
		if (ageInDays < 10) {
			return false;
		}
		return true;
	}
	
	@Override
	public void setDiscount() {
		this.setSellPrice(getSellPrice() * 0.7);
	}

	public static int getEggAmount() {
		return eggAmount;
	}
	
    

}
