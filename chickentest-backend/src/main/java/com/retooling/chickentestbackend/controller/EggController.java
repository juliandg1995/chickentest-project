package com.retooling.chickentestbackend.controller;

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

import com.retooling.chickentestbackend.exceptions.farm.NoFarmFoundException;
import com.retooling.chickentestbackend.model.Egg;
import com.retooling.chickentestbackend.model.Farm;
import com.retooling.chickentestbackend.services.EggService;
import com.retooling.chickentestbackend.services.FarmService;

import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/eggs")
public class EggController {
	
	@Autowired
	private EggService eggService;
	
	@Autowired 
	private FarmService farmService;
	
//	// Create a new egg 
//    @PostMapping(value = "/createEgg", produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<Egg> createEgg(@RequestBody Egg egg) {
//        if (egg.getSellPrice() < 0) {
//            return ResponseEntity.badRequest().build();
//        }
//        try {
//        	Egg newEgg = eggService.createEgg(egg);
//        	return ResponseEntity.ok(newEgg);        	
//
//        } catch (NoFarmFoundException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//        }
//    }
	
	// To get ALL eggs
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
    @GetMapping("/getEggsByFarm/{farmOwnerId}")
    public ResponseEntity<List<Egg>> getEggsByFarmOwnerId(@PathVariable Long farmOwnerId) {
    	try {
	        List<Egg> eggs = eggService.getAllEggsByFarmOwnerId(farmOwnerId);
	        return ResponseEntity.ok(eggs);
    	} catch (EntityNotFoundException e) {
    		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    	}
    }	

    @PostMapping("/eggs")
    public Egg createEgg(
            @RequestBody EggRequest eggRequest,
            @RequestParam("farmId") Long farmId) {
        return eggService.createEgg(eggRequest.getSellPrice(), farmId);
    }
    
    class EggRequest {
        private double sellPrice;

        public double getSellPrice() {
            return sellPrice;
        }

        public void setSellPrice(double sellPrice) {
            this.sellPrice = sellPrice;
        }
    }    
	
//	// To get ALL eggs
//	@GetMapping(value = "/getEggs")
//	public ResponseEntity<List<Egg>> getEggs() {
//		try {
//			List<Egg> eggs = eggService.getAllEggs();
//			return ResponseEntity.ok(eggs);
//		} catch (EntityNotFoundException e) {
//			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//		}
//	}	
//	
//	// To get ALL eggs searching BY FARM ID
//    @GetMapping("/getByFarm/{farmOwnerId}")
//    public ResponseEntity<List<Egg>> getEggsByFarmOwnerId(@PathVariable Farm farmOwner) {
//    	try {
//	        List<Egg> eggs = eggService.getAllEggsByFarmOwnerId(farmOwner.getId());
//	        return ResponseEntity.ok(eggs);
//    	} catch (EntityNotFoundException e) {
//    		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//    	}
//    }	
//    
//	// To get all UNHATCHED eggs searching BY FARM ID
//    @GetMapping("/getUnhatchedByFarm/{farmOwnerId}")
//    public ResponseEntity<List<Egg>> getAllUnhatchedEggsByFarmId(@PathVariable Farm farmOwner) {
//    	try {
//	        List<Egg> eggs = eggService.getAllUnhatchedEggsByFarmId(farmOwner.getId());
//	        return ResponseEntity.ok(eggs);
//    	} catch (EntityNotFoundException e) {
//    		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//    	}
//    }	
//    
//	// To get all UNHATCHED eggs searching BY FARM ID
//    @GetMapping("/getHatchedByFarm/{farmOwnerId}")
//    public ResponseEntity<List<Egg>> getAllHatchedEggsByFarmId(@PathVariable Farm farmOwner) {
//    	try {
//	        List<Egg> eggs = eggService.getAllHatchedEggsByFarmId(farmOwner.getId());
//	        return ResponseEntity.ok(eggs);
//    	} catch (EntityNotFoundException e) {
//    		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//    	}
//    }	
//	
	

}
