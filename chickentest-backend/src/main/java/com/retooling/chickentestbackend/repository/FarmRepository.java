package com.retooling.chickentestbackend.repository;

import org.springframework.data.repository.CrudRepository;
import java.util.Optional;
import java.util.List;

import com.retooling.chickentestbackend.model.Farm;
import com.retooling.chickentestbackend.model.Chicken;
import com.retooling.chickentestbackend.model.Egg;

public interface FarmRepository extends CrudRepository<Farm, Long>{
	Optional<Farm> findById(Long id);
	Optional<Double> findMoneyById(Long farmId);
	Optional<String> findNameById(Long farmId);
//	Optional<List<Egg>> findEggsByFarmId(Long farmId);
//	Optional<List<Chicken>> findChickensByFarmId(Long farmId);
	
}
