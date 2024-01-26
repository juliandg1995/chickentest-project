package com.retooling.chickentestbackend.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.retooling.chickentestbackend.model.Chicken;

public interface ChickenRepository extends CrudRepository<Chicken, Long>{
	
	// To get all Chickens 
	List<Chicken> findAll();
	 
	// Returns all chickens filtering by farmOwnerId 
	 List<Chicken> findByFarmOwner_Id(Long farmOwnerId);


}
