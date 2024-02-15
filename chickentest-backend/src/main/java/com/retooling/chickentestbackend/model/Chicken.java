package com.retooling.chickentestbackend.model;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "chickens")
public class Chicken extends Product {
	
    @Column
	private String name;
    
    @Column
    private int daysToEggsCountdown;
    
    @Column
    private int ageInDays;
    
    private boolean isNewBorn; 
    
    private static int daysToPutEggs = 10;
    
    private static int eggAmount = 1;
    
    private static double defaultSellPrice = 10.00;
    
	/** Constructors **/
    
	public Chicken() {
		super();
	}
    
	public Chicken(double sellPrice,  int age, Farm farmOwner) {
		super(sellPrice, farmOwner);
		this.isNewBorn = false;
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

	public int getAgeInDays() {
		return ageInDays;
	}
	
	public static int getEggAmount() {
		return eggAmount;
	}
	
	public static double getDefaultSellPrice(){
		return defaultSellPrice;
	}	
	
	public boolean getIsNewBorn() {
		return isNewBorn;
	}

	public void setAge(int age) {
		this.ageInDays = age;
	}
	
	public void setIsNewBorn(boolean value) {
		this.isNewBorn = value;
	}
	
	@Override
	public void passDays(int days) {
		
		for (int i = 0 ; i < days; i++) {
			if (!isNewBorn) {
				ageInDays++;
			}
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
    

}
