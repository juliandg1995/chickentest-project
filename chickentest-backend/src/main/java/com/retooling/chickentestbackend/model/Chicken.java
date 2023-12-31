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
    private int ageInYears;
    
    private static int daysToPutEggs = 15;
    
	/** Constructor **/
	public Chicken(double sellPrice,  int age, Farm farmOwner) {
		super(sellPrice, farmOwner);
		this.daysToEggsCountdown = daysToPutEggs;
		this.ageInYears = age;
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
		return ageInYears;
	}

	public void setAge(int age) {
		this.ageInYears = age;
	}
	
	@Override
	public void passADay() {
		this.daysToEggsCountdown++;
	}
	
	public void hatchEggs(List<Egg> eggs) {
		for(Egg egg: eggs) {
			egg.hatch();
		}
	}
	
	@Override
	public boolean isDiscountMaterial() {
		if (ageInYears < 4 || getDaysToPutEggs() <= 5) {
			return false;
		}
		return true;
	}
	
	@Override
	public void setDiscount() {
		this.setSellPrice(getSellPrice() * 0.7);
	}

	
    

}
