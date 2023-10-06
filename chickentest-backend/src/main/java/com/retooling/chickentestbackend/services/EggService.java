package com.retooling.chickentestbackend.services;

import java.util.List;
import java.util.stream.Collectors;

import org.hibernate.validator.internal.util.stereotypes.Lazy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.retooling.chickentestbackend.exceptions.farm.FailedCrudOperationException;
import com.retooling.chickentestbackend.exceptions.farm.FailedOperationException;
import com.retooling.chickentestbackend.exceptions.farm.FarmNotFoundException;
import com.retooling.chickentestbackend.exceptions.farm.NoFarmFoundException;
import com.retooling.chickentestbackend.model.Egg;
import com.retooling.chickentestbackend.model.Farm;
import com.retooling.chickentestbackend.repository.EggRepository;

import jakarta.transaction.Transactional;

@Service
//@Transactional
public class EggService {
	
	@Autowired
	private EggRepository eggRepository;
	
	@Autowired
	private FarmService farmService;
	
	
	@Transactional 
    public Egg createEgg(double sellPrice, Long farmId) throws NoFarmFoundException {
        // Retrieve the farm by ID
        Farm farm = farmService.getFarmById(farmId)
                .orElseThrow(() -> new NoFarmFoundException("ID"));

        // Create a new egg and set the farm as the owner
        Egg newEgg = new Egg(sellPrice, farm);

        // Save the egg to the database
        return eggRepository.save(newEgg);
    }

    // Delete the egg from the database
    public void deleteEgg(Long eggId) {
        eggRepository.deleteById(eggId);
    }
	
	// To get all eggs
    public List<Egg> getAllEggs() {
    	return eggRepository.findAll();
    }
	
    public List<Egg> getAllEggsByFarmOwnerId(Long farmOwnerId) {
        return eggRepository.findByFarmOwner_Id(farmOwnerId);
    }

    // Método para obtener todos los huevos no empollados
    public List<Egg> getAllUnhatchedEggs() {
        return eggRepository.findByIsHatchedFalse();
    }
    
    // Método para obtener todos los huevos empollados
    public List<Egg> getAllHatchedEggs() {
        return eggRepository.findByIsHatchedTrue();
    }
    
    
    // Método para obtener todos los huevos NO empollados de una granja por su ID
    public List<Egg> getAllUnhatchedEggsByFarmId(Long farmId) {
    	
    	List<Egg> eggs = this.getAllEggsByFarmOwnerId(farmId);
    	
    	if (eggs.isEmpty()) {
    		return null;
    	}
    	
        // Filtrado de huevos no empollados
        return eggs.stream()
                .filter(egg -> !egg.getIsHatched())
                .collect(Collectors.toList());
    }
    

    // Método para obtener todos los huevos empollados de una granja por su ID
    public List<Egg> getAllHatchedEggsByFarmId(Long farmId) {
    	
    	List<Egg> eggs = this.getAllEggsByFarmOwnerId(farmId);
    	
    	if (eggs.isEmpty()) {
    		return null;
    	}
    	
        // Filtrado de huevos empollados
        return eggs.stream()
                .filter(egg -> egg.getIsHatched())
                .collect(Collectors.toList());
    }
    

    public void passDays(int days){
    	this.getAllEggs().forEach(egg -> {
    		egg.passDays(days);
    		if (egg.getIsEcloded()) {
    			this.deleteEgg(egg.getId());
    			// *** Falta que #manageEclodeEgg capture las excepciones que genera
//    		    farmService.manageEclodedEgg(egg);
    		}
    	});
    }

}
