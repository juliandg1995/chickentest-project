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

@RestController()
@RequestMapping(value = "/api")
public class ChickenController {
	
	@Autowired
	private ChickenService chickenService;
	
//	@PostMapping(value = "/createChicken", produces = MediaType.APPLICATION_JSON_VALUE)
//	public ResponseEntity<Chicken> createChicken(@RequestBody double sell_price, int age) {
//
//		return ResponseEntity.ok(chickenService.createChicken(sell_price, age));
//	}


}
