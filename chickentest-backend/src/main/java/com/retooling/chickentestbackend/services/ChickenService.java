package com.retooling.chickentestbackend.services;

import jakarta.transaction.Transactional;

import com.retooling.chickentestbackend.exceptions.farm.NoFarmFoundException;
import com.retooling.chickentestbackend.model.*;
import com.retooling.chickentestbackend.repository.ChickenRepository;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChickenService {
	
	@Autowired
	private ChickenRepository chickenRepository;
	
	@Autowired
	private FarmService farmService;	
	

//	@Transactional
//	public Chicken createChicken(Chicken newChicken) throws NoFarmFoundException {
//		
//		// Retrieving farm using farm ID
//        Farm farmOwner = farmService.getFarmById(newChicken.getfarmOwner().getId())
//                .orElseThrow(() -> new NoFarmFoundException("ID"));
//        
//        // Creating chicken
//		return chickenRepository.save(newChicken);
//		
//	}
	
  // To get all chickens
  public List<Chicken> getAllChickens() {
  	return chickenRepository.findAll();
  }
	
  public List<Chicken> getAllChickensByFarmOwnerId(Long farmOwnerId) {
      return chickenRepository.findByFarmOwner_Id(farmOwnerId);
  }



}
