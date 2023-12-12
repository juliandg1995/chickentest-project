package com.retooling.chickentestbackend.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.retooling.chickentestbackend.model.Farm;

public interface FarmRepository extends CrudRepository<Farm, Long>{
//	Optional<Farm> findById(Long id);
	Optional<Double> findMoneyById(Long farmId);
	Optional<String> findNameById(Long farmId);
//	Optional<List<Egg>> findEggsByFarmOwner_Id(Long farmId);
//	Optional<List<Chicken>> findChickensByFarmId(Long farmId);
//	Optional<Farm> addChickensToFarm(Long farmId, List<Chicken> newChickens);
	
}
