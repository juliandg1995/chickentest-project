package com.retooling.chickentestbackend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.retooling.chickentestbackend.model.Farm;
import com.retooling.chickentestbackend.model.Chicken;
import com.retooling.chickentestbackend.services.ChickenService;
import com.retooling.chickentestbackend.services.FarmService;

import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/chickens")
public class ChickenController {
	
	@Autowired
	private ChickenService chickenService;
	
	private FarmService farmService;
	
//	@PostMapping(value = "/createChicken", produces = MediaType.APPLICATION_JSON_VALUE)
//	public ResponseEntity<Chicken> createChicken(@RequestBody double sell_price, int age) {
//
//		return ResponseEntity.ok(chickenService.createChicken(sell_price, age));
//	}
	
	// To get ALL chickens
	// Este funciona bien
	@GetMapping(value = "/getChickens")
	public ResponseEntity<List<Chicken>> getChickens() {
		try {
			List<Chicken> chickens = chickenService.getAllChickens();
			return ResponseEntity.ok(chickens);
		} catch (EntityNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
	}	
	
	// To get ALL chickens searching BY FARM ID
	// Este también funciona bien
    @GetMapping("/getChickensByFarmId/{farmOwnerId}")
    public ResponseEntity<List<Chicken>> getChickensByFarmOwnerId(@PathVariable Long farmOwnerId) {
    	try {
	        List<Chicken> chickens = chickenService.getAllChickensByFarmOwnerId(farmOwnerId);
	        return ResponseEntity.ok(chickens);
    	} catch (EntityNotFoundException e) {
    		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    	}
    }	

}
