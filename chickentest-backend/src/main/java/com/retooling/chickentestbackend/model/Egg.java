package com.retooling.chickentestbackend.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;
import jakarta.transaction.Transactional;

@Entity
@Table(name = "eggs")
public class Egg extends Product {

	private static int daysToEclode = 10;

	private int chickenCountdown;

	private boolean isHatched;

	private boolean isEcloded;

	@Column(name = "ageInDays")
	private int ageInDays;

	public Egg() {
		super();
	}

	public Egg(double sellPrice, Farm farmOwner) {
		super(sellPrice, farmOwner);
		this.chickenCountdown = daysToEclode;
		isHatched = false;
		isEcloded = false;
		ageInDays = 0;
	}

	public int getDaysToEclode() {
		return daysToEclode;
	}

	public int getAgeInDays() {
		return this.ageInDays;
	}

	public void hatch() {
		this.isHatched = true;
	}

	public boolean getIsHatched() {
		return this.isHatched;
	}

	public boolean getIsEcloded() {
		return isEcloded;
	}

	@Override
	public void passDays(int days) {
		for (int i = 0; i < days; i++) {
			if (isHatched) {
				this.chickenCountdown--;
				if (chickenCountdown == 0) {
					this.eclode();
				}
			}
			if (!isEcloded) {
				ageInDays++;
			} else {
				break;
			}
		}
	}


	@Override
	public boolean isDiscountMaterial() {
		if (isHatched || this.getAgeInDays() < 15) {
			return false;
		}
		return true;
	}

	@Override
	public void setDiscount() {
		this.setSellPrice(getSellPrice() * 0.5);
	}

	private void eclode() {
		isEcloded = true;
	}

}
