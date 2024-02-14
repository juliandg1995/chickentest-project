package com.retooling.chickentestbackend.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.retooling.chickentestbackend.model.Egg;

@Repository
public interface EggRepository extends CrudRepository<Egg, Long>{
	
	// To get all eggs 
	List<Egg> findAll();
	
	 // Returns all eggs filtering by farmOwnerId 
	 List<Egg> findByFarmOwner_Id(Long farmOwnnerId);
	 
	 // Returns all non-hatched eggs
	 List<Egg> findByIsHatchedFalse();

	 // Returns all hatched eggs
	 List<Egg> findByIsHatchedTrue();
	 
	 // Método para obtener todos los huevos no empollados de una granja por su ID
	 List<Egg> findByFarmOwner_IdAndIsHatchedFalse(Long farmId);
	 
}
