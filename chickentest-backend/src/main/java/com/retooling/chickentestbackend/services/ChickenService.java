package com.retooling.chickentestbackend.services;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

		List<Chicken> chickens = chickenRepository.findAll();

		return chickens.stream().peek(c -> {
			c.passDays(numberOfDays);
			chickenRepository.save(c);
		}).filter(c -> c.getDaysToEggsCountdown() == 0 && c.getAge() != 0).map(c -> {
			c.resetDaysToEggsCountdown();
			return c;
		}).flatMap(c -> Stream.of(new Egg(c.getSellPrice(), c.getfarmOwner()))).collect(Collectors.toList());
	}

// public String passDays(int days) {
//	 this.getAllChickens().forEach(chicken -> {
// 		chicken.passDays(days);
// 		if (chicken.getDaysToEggsCountdown() == 0) {
// 			try {
//// 				this.deleteEgg(egg.getId()); -> No hace falta borrar de BDD
//     		    farmService.manageNewEggs(chicken.getfarmOwner().getId());
// 			} catch(FailedOperationException e) {
// 				e = new FailedOperationException("New eggs creation");
// 				System.err.println(e.getMessage());
// 			}
// 		}
// 	});
// 	return days + " passed by successfully";	 
// }

}
