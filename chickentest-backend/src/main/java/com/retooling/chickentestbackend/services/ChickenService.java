package com.retooling.chickentestbackend.services;

import jakarta.transaction.Transactional;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.retooling.chickentestbackend.exceptions.farm.FailedOperationException;
import com.retooling.chickentestbackend.exceptions.farm.FarmNotFoundException;
import com.retooling.chickentestbackend.model.*;
import com.retooling.chickentestbackend.repository.ChickenRepository;

import java.util.List;
import java.util.stream.Collectors;

import org.hibernate.validator.internal.util.stereotypes.Lazy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChickenService {
	
	@Autowired
	private ChickenRepository chickenRepository;
	
	@Autowired
	private FarmService farmService;	
	
	
	@Transactional 
    public Chicken createChicken(double sellPrice, int age, Long farmId) throws FarmNotFoundException {
        // Retrieve the farm by ID
        Farm farm = farmService.getFarmById(farmId)
                .orElseThrow(() -> new FarmNotFoundException(farmId));

        // Create a new chicken and set the farm as the owner
        Chicken newChicken = new Chicken(sellPrice, age, farm);

        // Save the egg to the database
        return chickenRepository.save(newChicken);
    }	
	
  // To get all chickens
  public List<Chicken> getAllChickens() {
  	return chickenRepository.findAll();
  }
	
  public List<Chicken> getAllChickensByFarmOwnerId(Long farmOwnerId) {
      return chickenRepository.findByFarmOwner_Id(farmOwnerId);
  }

 public String passDays(int days) {
	 this.getAllChickens().forEach(chicken -> {
 		chicken.passDays(days);
 		if (chicken.getDaysToEggsCountdown() == 0) {
 			try {
// 				this.deleteEgg(egg.getId()); -> No hace falta borrar de BDD
     		    farmService.manageNewEggs();
 			} catch(FailedOperationException e) {
 				e = new FailedOperationException("New eggs creation");
 				System.err.println(e.getMessage());
 			}
 		}
 	});
 	return days + " passed by successfully";	 
 }


}
