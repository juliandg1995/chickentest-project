package com.retooling.chickentestbackend.services;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.retooling.chickentestbackend.exceptions.farm.FailedOperationException;
import com.retooling.chickentestbackend.exceptions.farm.FarmNotFoundException;
import com.retooling.chickentestbackend.exceptions.farm.InsufficientPaymentException;
import com.retooling.chickentestbackend.exceptions.farm.InsufficientStockException;
import com.retooling.chickentestbackend.exceptions.farm.IterationException;
import com.retooling.chickentestbackend.exceptions.farm.MaxStockException;
import com.retooling.chickentestbackend.exceptions.farm.NegativeValuesException;
import com.retooling.chickentestbackend.exceptions.farm.NoChickensException;
import com.retooling.chickentestbackend.exceptions.farm.NoEggsException;
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
	public Egg createEgg(double sellPrice, Long farmId) throws FarmNotFoundException {
		// Retrieve the farm by ID
		Farm farm = farmService.getFarmById(farmId).orElseThrow(() -> new FarmNotFoundException(farmId));

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
		return eggs.stream().filter(egg -> !egg.getIsHatched()).collect(Collectors.toList());
	}

	// Método para obtener todos los huevos empollados de una granja por su ID
	public List<Egg> getAllHatchedEggsByFarmId(Long farmId) {

		List<Egg> eggs = this.getAllEggsByFarmOwnerId(farmId);

		if (eggs.isEmpty()) {
			return null;
		}

		// Filtrado de huevos empollados
		return eggs.stream().filter(egg -> egg.getIsHatched()).collect(Collectors.toList());
	}
	
	public boolean eggStockControl(Long farmId) {
		return this.getAllEggsByFarmOwnerId(farmId).size() < Farm.getMaxStockOfEggs();
	}
	
	public void addEggs(List<Egg> newEggs) {
		this.eggRepository.saveAll(newEggs);
	}
	
    public String passDays(int days) throws NegativeValuesException, IterationException, MaxStockException {

        try {
            AtomicBoolean shouldCancel = new AtomicBoolean(false);
           
            // For Eggs
            this.getAllEggs().forEach(egg -> {
                if (shouldCancel.get()) {
                    return; 
                }
                try {
                	int chickenAge = days - egg.getChickenCountdown();
                    egg.passDays(days);
                    if (egg.getIsEcloded()) {
                    	farmService.manageEclodedEgg(egg, chickenAge);
                    } else {
                    	eggRepository.save(egg);
                    }
                } catch ( NegativeValuesException| FarmNotFoundException | FailedOperationException | MaxStockException | InsufficientStockException | NoChickensException | InsufficientPaymentException e) {
                    shouldCancel.set(true);
                    throw new RuntimeException(e.getMessage());
                }
            });
        } catch (RuntimeException e) {
           throw new IterationException(e.getMessage());
        }
        return days + " passed by successfully";
    }	

	@SuppressWarnings("unused")
	public void hatchEggs(List<Long> eggsId) throws NoEggsException, FailedOperationException {
		
		boolean isEmpty = true;
		
		Iterable<Egg> eggsIterable = eggRepository.findAllById(eggsId);
		
		for(Egg egg : eggsIterable) {
			isEmpty = false;
			break;
		}
		
		if (!isEmpty) {
			List<Egg> eggsList = StreamSupport
					 .stream(eggsIterable.spliterator(), false)
					 .map(e -> { e.hatch(); return e; })
					 .collect(Collectors.toList());
			
			if (eggsList.size() != eggsId.size()) {
				throw new FailedOperationException("Hatching eggs");
			}
			
			eggRepository.saveAll(eggsList);
			
		} else {
			throw new NoEggsException();
		}
		
	}
	
	@SuppressWarnings("unused")
	public void unhatchEggs(List<Long> eggsId) throws NoEggsException, FailedOperationException {
		
		boolean isEmpty = true;
		
		Iterable<Egg> eggsIterable = eggRepository.findAllById(eggsId);
		
		for(Egg egg : eggsIterable) {
			isEmpty = false;
			break;
		}
		
		if (!isEmpty) {
			List<Egg> eggsList = StreamSupport
					 .stream(eggsIterable.spliterator(), false)
					 .map(e -> { e.unhatch(); return e; })
					 .collect(Collectors.toList());
			
			if (eggsList.size() != eggsId.size()) {
				throw new FailedOperationException("Unhatching eggs");
			}
			
			eggRepository.saveAll(eggsList);
			
		} else {
			throw new NoEggsException();
		}
	}
	

}
