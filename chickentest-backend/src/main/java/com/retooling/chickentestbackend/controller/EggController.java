package com.retooling.chickentestbackend.controller;

import java.security.InvalidParameterException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.retooling.chickentestbackend.dto.EggRequestDTO;
import com.retooling.chickentestbackend.exceptions.farm.FailedOperationException;
import com.retooling.chickentestbackend.exceptions.farm.FarmNotFoundException;
import com.retooling.chickentestbackend.exceptions.farm.NoEggsException;
import com.retooling.chickentestbackend.model.Egg;
import com.retooling.chickentestbackend.model.Farm;
import com.retooling.chickentestbackend.services.EggService;
import com.retooling.chickentestbackend.services.FarmService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/eggs")
public class EggController {
	
	@Autowired
	private EggService eggService;
	
	@Autowired 
	private FarmService farmService;
	
	// Funciona utilizando un objeto DTO (en package chickentestbackend.dto)
	@Transactional
    @PostMapping(value = "/createEgg", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Egg> createEgg(
            @RequestBody EggRequestDTO eggRequest) {
    	try {
    		Egg newEgg = eggService.createEgg(eggRequest.getSellPrice(), eggRequest.getFarmId());
    		return ResponseEntity.ok(newEgg);
    	} catch(FarmNotFoundException e) {
    		 return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    	}
    }		
	
	// To get ALL eggs
	// Este funciona
	@GetMapping(value = "/getEggs")
	public ResponseEntity<List<Egg>> getEggs() {
		try {
			List<Egg> eggs = eggService.getAllEggs();
			return ResponseEntity.ok(eggs);
		} catch (EntityNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
	}	

	// To get ALL eggs searching BY FARM ID
	// Este también funciona
    @GetMapping(value = "/getEggsByFarmId/{farmOwnerId}")
    public ResponseEntity<List<Egg>> getEggsByFarmOwnerId(@PathVariable Long farmOwnerId) {
    	try {
	        List<Egg> eggs = eggService.getAllEggsByFarmOwnerId(farmOwnerId);
	        return ResponseEntity.ok(eggs);
    	} catch (EntityNotFoundException e) {
    		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    	}
    }		

	// To get all UNHATCHED eggs searching BY FARM ID
    @GetMapping(value = "/getUnhatchedEggsByFarmId/{farmId}")
    public ResponseEntity<List<Egg>> getUnhatchedEggsByFarmId(@PathVariable Long farmId) {
    	try {
	        List<Egg> eggs = eggService.getAllUnhatchedEggsByFarmId(farmId);
	        return ResponseEntity.ok(eggs);
    	} catch (EntityNotFoundException e) {
    		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    	}
    }	
    
	// To get all HATCHED eggs searching BY FARM ID
    @GetMapping(value ="/getHatchedEggsByFarmId/{farmId}")
    public ResponseEntity<List<Egg>> getHatchedEggsByFarmId(@PathVariable Long farmId) {
    	try {
	        List<Egg> eggs = eggService.getAllHatchedEggsByFarmId(farmId);
	        return ResponseEntity.ok(eggs);
    	} catch (EntityNotFoundException e) {
    		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    	}
    }	
    
    @PostMapping("/hatchEggs")
    public ResponseEntity<?> hatchEggs(@RequestBody List<Long> eggsId) throws NoEggsException, FailedOperationException {
        try {
            eggService.hatchEggs(eggsId);
            return ResponseEntity.ok(eggsId);
        } catch (NoEggsException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (FailedOperationException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
	

}
