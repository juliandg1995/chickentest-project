package com.retooling.chickentestbackend.model;

import java.util.*;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;

@Entity
@Table(name="farm")
public class Farm {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private Long id;
	
	private String name;
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "farmOwner")
	@JsonManagedReference
	private List<Egg> eggs;
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "farmOwner" )
	@JsonManagedReference
	private List<Chicken> chickens;
	
	private double money;
	
	private static int maxChickenStock = 500;
	
	private static int maxEggStock = 50000;
	
	public Farm() {
	}
	
	public Farm(String name, double money) {
		super();
		this.name = name;
		this.money = money;
		chickens = new ArrayList<Chicken>();
		eggs = new ArrayList<Egg>();
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String aName) {
		name = aName;
	}
	
	public Long getId() {
		return id;
	}
	
	public double getMoney(){
		return money;
	}
	
	public static int getMaxStockOfChickens() {
		return maxChickenStock;
	}
	
	public static int getMaxStockOfEggs() {
		return maxEggStock;
	}
	
	public List<Chicken> getChickens(){
		return chickens;
	}
	
	public List<Egg> getEggs(){
		return eggs;
	}
	
	public void passADay(){
		
	}
	
	public void eggToChicken(Egg anEgg) {
		
	}
	
	
	
	
	
	

	
	
	

}
