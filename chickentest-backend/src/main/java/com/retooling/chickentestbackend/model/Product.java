package com.retooling.chickentestbackend.model;

import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	@Column(name = "id", updatable = false, nullable = false)
	private Long id;

	@Column(name = "sellPrice")
	private double sellPrice;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "farmOwner")
	@JsonBackReference
	private Farm farmOwner;

	/******* Constructors *******/

	public Product() {
	}

	@Autowired
	public Product(double sellPrice, Farm farmOwner) {
		this.sellPrice = sellPrice;
		this.farmOwner = farmOwner;
	}

	/****** Getters / Setters ******/	

	public Long getId() {
		return id;
	}

	public double getSellPrice() {
		return sellPrice;
	}

	public void setSellPrice(double sellPrice) {
		this.sellPrice = sellPrice;
	}

	public void setfarmOwner(Farm newfarmOwner) {
		this.farmOwner = newfarmOwner;
	}

	public Farm getfarmOwner() {
		return farmOwner;
	}

	public abstract void passDays(int days);


}
