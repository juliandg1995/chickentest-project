package com.retooling.chickentestbackend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.retooling.chickentestbackend.model.Chicken;
import com.retooling.chickentestbackend.services.ChickenService;
import com.retooling.chickentestbackend.services.FarmService;

import jakarta.persistence.EntityNotFoundException;

@Controller
@RequestMapping("/chickens")
public class ChickenController {
	
	@Autowired
	private ChickenService chickenService;
	
	@SuppressWarnings("unused")
	private FarmService farmService;
	
	
	// To get ALL chickens
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
    @GetMapping("/getChickensByFarmId/{farmOwnerId}")
    public ResponseEntity<List<Chicken>> getChickensByFarmOwnerId(@PathVariable Long farmOwnerId) {
    	try {
	        List<Chicken> chickens = chickenService.getAllChickensByFarmOwnerId(farmOwnerId);
	        return ResponseEntity.ok(chickens);
    	} catch (EntityNotFoundException e) {
    		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    	}
    }	
    
	////////////////////////////////////////////////
	///// 			FORM CONTROLLERS 		   /////
	////////////////////////////////////////////////
	
	 @GetMapping("/getChickensByFarmIdForm")
	    public String showChickensByFarmId(@RequestParam Long farmOwnerId, Model model) {
	        try {
	            List<Chicken> chickens = chickenService.getAllChickensByFarmOwnerId(farmOwnerId);
	            model.addAttribute("chickens", chickens);
	            return "listChickensForm";  
	        } catch (EntityNotFoundException e) {
	        	model.addAttribute("message", e.getMessage());
	            return "listChickensForm";
	        }
	    }    

}
