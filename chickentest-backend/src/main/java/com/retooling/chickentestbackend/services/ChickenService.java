package com.retooling.chickentestbackend.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.retooling.chickentestbackend.exceptions.farm.FarmNotFoundException;
import com.retooling.chickentestbackend.model.Chicken;
import com.retooling.chickentestbackend.model.Egg;
import com.retooling.chickentestbackend.model.Farm;
import com.retooling.chickentestbackend.repository.ChickenRepository;

import jakarta.transaction.Transactional;

@Service
public class ChickenService {

	@Autowired
	private ChickenRepository chickenRepository;

	@Autowired
	private FarmService farmService;

	@Transactional
	public Chicken createChicken(double sellPrice, int age, Long farmId) throws FarmNotFoundException {
		// Retrieve the farm by ID
		Farm farm = farmService.getFarmById(farmId).orElseThrow(() -> new FarmNotFoundException(farmId));

		// Create a new chicken and set the farm as the owner
		Chicken newChicken = new Chicken(sellPrice, age, farm);

		// Save the egg to the database
		return chickenRepository.save(newChicken);
	}

	@Transactional
	public void deleteChicken(Long chickenId) {
		chickenRepository.deleteById(chickenId);
	}

	// To get all chickens
	public List<Chicken> getAllChickens() {
		return chickenRepository.findAll();
	}

	public List<Chicken> getAllChickensByFarmOwnerId(Long farmOwnerId) {
		return chickenRepository.findByFarmOwner_Id(farmOwnerId);
	}

	public List<Egg> passDays(int numberOfDays) {
		
		List<Chicken> chickens = getAllChickens();
		
		for(Chicken chicken : chickens) {
			int totalDays = numberOfDays;
			while (totalDays > 0) {
				int daysCounter = chicken.getDaysToEggsCountdown();
				chicken.passDays(numberOfDays);
				
				totalDays = daysCounter;
				
				if (chicken.getDaysToEggsCountdown() == 0 && chicken.getAgeInDays() != 0 ) {
					chicken.resetDaysToEggsCountdown();
					farmService.addEggToFarmFromChicken(chicken);
				}
				
				chickenRepository.save(chicken);
				
			}
		}

//		return chickens.stream().peek(c -> { 
//		c.passDays(numberOfDays); 
//		chickenRepository.save(c);
//	 	}).filter(c -> c.getDaysToEggsCountdown() == 0 && c.getAgeInDays() != 0)
//	    .map(c -> { c.resetDaysToEggsCountdown(); chickenRepository.save(c); return c; })	
//       .flatMap(c -> Stream.of(new Egg(farmService.getEggsPrice(c.getFarmOwner()), c.getFarmOwner()))).collect(Collectors.toList());
		
		List<Egg> newEggs = new ArrayList<Egg>();

		for (Chicken chicken : chickens) {
			int totalDays = numberOfDays;
			while (totalDays > 0) {
				int daysCounter = chicken.getDaysToEggsCountdown();
				chicken.passDays(totalDays);
				
				totalDays -= daysCounter;

				if (chicken.getDaysToEggsCountdown() == 0 && chicken.getAgeInDays() != 0) {
					chicken.resetDaysToEggsCountdown();

					Egg egg = new Egg(farmService.getEggsPrice(chicken.getFarmOwner()), chicken.getFarmOwner());
					newEggs.add(egg);
				}
				chickenRepository.save(chicken);
			}
		}
		return newEggs;
	}
}
