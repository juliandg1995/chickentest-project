package com.retooling.chickentestbackend.model;

import java.util.*;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

@Entity
@Table(name="farms")
public class Farm {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private Long id;
	
	@NotBlank(message = "Farm name cannot be blank")
	@Size(max = 255, message = "Farm name should not exceed 255 characters")
	private String name;
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "farmOwner")
	@JsonManagedReference
	private List<Egg> eggs;
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "farmOwner" )
	@JsonManagedReference
	private List<Chicken> chickens;
	
	@Positive(message = "Money value should be positive")
	private double money;
	
	private static int maxChickenStock = 3;
	
	private static int maxEggStock = 5;
	
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
	
	public void setChickens(List<Chicken> chickens){
		this.chickens = chickens;
	}
	
	public List<Egg> getEggs(){
		return eggs;
	}
	
	public void setEggs(List<Egg> eggs) {
		this.eggs = eggs;
	}
	
	public void spendMoney(double amount) {
		money -= amount;
	}
	
	public void earnMoney(double amount) {
		money += amount;
	}
	
	
	
	
	
	
	
	

	
	
	

}
