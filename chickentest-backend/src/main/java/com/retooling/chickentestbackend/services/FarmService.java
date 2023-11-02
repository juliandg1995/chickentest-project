package com.retooling.chickentestbackend.services;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.retooling.chickentestbackend.exceptions.farm.ChickenNotFoundException;
import com.retooling.chickentestbackend.exceptions.farm.EggNotFoundException;
import com.retooling.chickentestbackend.exceptions.farm.FailedOperationException;
import com.retooling.chickentestbackend.exceptions.farm.FarmNotFoundException;
import com.retooling.chickentestbackend.exceptions.farm.InsufficientMoneyException;
import com.retooling.chickentestbackend.exceptions.farm.InsufficientPaymentException;
import com.retooling.chickentestbackend.exceptions.farm.InsufficientStockException;
import com.retooling.chickentestbackend.exceptions.farm.IterationException;
import com.retooling.chickentestbackend.exceptions.farm.MaxStockException;
import com.retooling.chickentestbackend.exceptions.farm.NoChickensException;
import com.retooling.chickentestbackend.exceptions.farm.NoEggsException;
import com.retooling.chickentestbackend.model.Chicken;
import com.retooling.chickentestbackend.model.Egg;
import com.retooling.chickentestbackend.model.Farm;
import com.retooling.chickentestbackend.repository.EggRepository;
import com.retooling.chickentestbackend.repository.FarmRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional; // -> Investigar qué hace y cuándo se usa

@Service
public class FarmService {

	//// Dependencies

//	@Autowired
	private FarmRepository farmRepository;
//
//	@Autowired		
	private ChickenService chickenService;
//	
//	@Autowired 
	private EggService eggService;

	public FarmService(@Lazy FarmRepository farmRepository, @Lazy ChickenService chickenService,
			@Lazy EggService eggService) {
		this.farmRepository = farmRepository;
		this.chickenService = chickenService;
		this.eggService = eggService;
	}

	//// Methods

	@Transactional
	public Farm createFarm(String name, double money) {
		try {
			Farm newFarm = new Farm(name, money);
			return farmRepository.save(newFarm);
		} catch (Exception e) {
			new Exception();
			return null;
		}
	}

	@Transactional
	public void deleteFarmById(Long farmId) {
		farmRepository.deleteById(farmId);
	}

	public List<Farm> getAllFarms() {
		return (List<Farm>) farmRepository.findAll();
	}

	// Returns a farm, searching by ID
	@SuppressWarnings("removal")
	public Optional<Farm> getFarmById(Long farmId) throws FarmNotFoundException {
		Optional<Farm> farmOptional = farmRepository.findById(farmId);
		if (!farmOptional.isPresent()) {
			throw new FarmNotFoundException(farmId);
		}
		return farmOptional;
	}

	public String getFarmSummaryById(Long farmId) throws FarmNotFoundException {

		// Searching Farm by Id
		Optional<Farm> farmOptional = farmRepository.findById(farmId);

		if (farmOptional.isPresent()) {
			Farm farm = farmOptional.get();

			// If farm exists, get its properties info
			String farmName = farm.getName();
			Long farmIdCode = farm.getId();
			double farmMoney = farm.getMoney();
			int chickenCount = farm.getChickens().size();
			int eggCount = farm.getEggs().size();

			// Build the string with the info
			String farmSummary = "Farm Name: " + farmName + "\n";
			farmSummary += "Id: " + farmIdCode + "\n";
			farmSummary += "Money: " + farmMoney + "\n";
			farmSummary += "Number of Chickens: " + chickenCount + "\n";
			farmSummary += "Number of Eggs: " + eggCount + "\n";

			return farmSummary;
		} else {
			// If not found, throw exception with farm's missing information ("ID")
			throw new FarmNotFoundException(farmId);
		}
	}

	public Double getMoneyById(Long farmId) {
		Optional<Double> moneyOptional = farmRepository.findMoneyById(farmId);

		return moneyOptional.orElse(null);
	}

	public String getNameById(Long farmId) {
		Optional<String> nameOptional = farmRepository.findNameById(farmId);

		return nameOptional.orElse(null);
	}
	
	public int getEggStocSizekFrom(Long farmId) {
		return eggService.getAllEggsByFarmOwnerId(farmId).size();
	}
	
	public int getChickenStockSizeFrom(Long farmId) {
		return chickenService.getAllChickensByFarmOwnerId(farmId).size();
	}

	@Transactional
	public void removeEggFromList(Long farmId, Egg eggToRemove) throws EggNotFoundException, FarmNotFoundException {
		Optional<Farm> optionalFarm = farmRepository.findById(farmId);
		if (optionalFarm.isPresent()) {
			Farm farm = optionalFarm.get();
			List<Egg> eggs = farm.getEggs();
			boolean removed = eggs.remove(eggToRemove); // Remove the specific egg object from list
			if (!removed) {
				throw new EggNotFoundException(eggToRemove.getId());
			}
			farmRepository.save(farm); // Save the updated Farm object
			eggService.deleteEgg(eggToRemove.getId());
			
		} else {
			throw new FarmNotFoundException(farmId);
		}
	}

	@Transactional
	public void removeChickenFromList(Long farmId, Chicken chickenToRemove)
			throws ChickenNotFoundException, FarmNotFoundException {
		Optional<Farm> optionalFarm = farmRepository.findById(farmId);
		if (optionalFarm.isPresent()) {
			Farm farm = optionalFarm.get();
			List<Chicken> chickens = farm.getChickens();
			boolean removed = chickens.remove(chickenToRemove);
			if (!removed) {
				throw new ChickenNotFoundException(chickenToRemove.getId());
			}
			farmRepository.save(farm);
		} else {
			throw new FarmNotFoundException(farmId);
		}
	}

	@Transactional
	public void addEggToFarmList(Egg newEgg, Long farmId) throws FarmNotFoundException, FailedOperationException {
		Optional<Farm> optionalFarm = farmRepository.findById(farmId);
		if (optionalFarm.isPresent()) {
			Farm farm = optionalFarm.get();
			List<Egg> eggs = farm.getEggs();
			boolean added = eggs.add(newEgg); // Remove the specific egg object from list
			if (!added) {
				throw new FailedOperationException("Edding egg to farm " + farm.getId() + " list");
			}
			farmRepository.save(farm); // Save the updated Farm object
		} else {
			throw new FarmNotFoundException(farmId);
		}
	}

	@Transactional
	public void addChickenToFarmList(Chicken newChicken, Long farmId)
			throws FarmNotFoundException, FailedOperationException {
		Optional<Farm> optionalFarm = farmRepository.findById(farmId);
		if (optionalFarm.isPresent()) {
			Farm farm = optionalFarm.get();
			List<Chicken> chickens = farm.getChickens();
			boolean added = chickens.add(newChicken); // Remove the specific egg object from list
			if (!added) {
				throw new FailedOperationException("Adding chicken to farm " + farm.getId() + " list");
			}
			farmRepository.save(farm); // Save the updated Farm object
		} else {
			throw new FarmNotFoundException(farmId);
		}
	}


	@SuppressWarnings("removal")
	@Transactional
	public void manageEclodedEgg(Egg anEclodedEgg) throws FarmNotFoundException, FailedOperationException {
		// Con el Cascade = ALL de las listas en Farm, se elimina automáticamente de BDD
		// al eliminar de lista
		// FailedOperationException tiene que ser atrapada aquí con un try/catch, para
		// no jorobar el forEach
		Long farmOwnerId = anEclodedEgg.getfarmOwner().getId();
		if (farmOwnerId.equals(null)) {
			throw new FarmNotFoundException(0L);
		}
		double chickenPrice = chickenService.getAllChickensByFarmOwnerId(farmOwnerId).get(0).getSellPrice();
		this.removeEggFromList(farmOwnerId, anEclodedEgg);
		Chicken newChicken = chickenService.createChicken(chickenPrice, 0, farmOwnerId);
		this.addChickenToFarmList(newChicken, farmOwnerId);
	}

	@Transactional
	public String manageNewEggs(Long farmId) throws FailedOperationException {
		try {
//			Optional<Farm> farmOptional = this.getFarmById(farmId);
//			Farm farm = farmOptional.get();
			Farm farm = farmRepository.findById(farmId).get();
			double sellPrice = farm.getEggs().get(0).getSellPrice();
			for (int i = 0; i < Chicken.getEggAmount(); i++) {
				eggService.createEgg(sellPrice, farmId);
			}
			return "OK";
		} catch (EntityNotFoundException e) {
			return e.getMessage();
		}
	}

	public void passDays(int numberOfDays) throws FailedOperationException, InvalidParameterException, IterationException {
		// For Eggs
		if (numberOfDays < 1) {
			throw new InvalidParameterException();
		}
//		eggService.passDays(numberOfDays);
//		chickenService.passDays(numberOfDays);
		
		eggService.passDays(numberOfDays);
		List<Egg> newEggs = chickenService.passDays(numberOfDays);
		newEggs.stream().forEach(e -> eggService.createEgg(e.getSellPrice(), e.getfarmOwner().getId()));
		
	}
	
	@Transactional
	public void addNewEggsForFarm(Farm farm, int amount) {
		double sellPrice;
		List<Egg> eggs = farm.getEggs();
		if (eggs.isEmpty()) {
			sellPrice = Egg.getDefaultSellPrice();
		} else {
			sellPrice = eggs.get(0).getSellPrice();
		}
		for(int i = 0; i < amount; i++) {
			eggs.add(eggService.createEgg(sellPrice, farm.getId()));			
		}
		farmRepository.save(farm);
	}
	
	@Transactional
	public void addNewChickensForFarm(Farm farm, int amount) {
		double sellPrice;
		List<Chicken> chickens = farm.getChickens();
		if (chickens.isEmpty()) {
			sellPrice = Chicken.getDefaultSellPrice();
		} else {
			sellPrice = chickens.get(0).getSellPrice();
		}
		for(int i = 0; i < amount; i++) {
			chickens.add(chickenService.createChicken(sellPrice, 0, farm.getId()));			
		}
		farmRepository.save(farm);
	}
	
	@Transactional
	public void buyNewEggs(int amount, double price, Long forFarmId) 
		   throws InsufficientMoneyException, 
		   		  NoEggsException, 
		   		  MaxStockException, 
		   		  FarmNotFoundException {
		
		Farm farm = this.getFarmById(forFarmId).orElseThrow(() -> new FarmNotFoundException(forFarmId));
		
		int stock = eggService.getAllEggsByFarmOwnerId(forFarmId).size();
		
		if ( stock < stock + amount ) {
			throw new MaxStockException("egg");
		}
		
		double money = farm.getMoney();
		double total_cost = price * amount;
		
		if (money < total_cost) {
			throw new InsufficientMoneyException();
		}
		
	    //Cattle and money amount update
	    this.addNewEggsForFarm(farm, amount);
	    farm.spendMoney(total_cost);
	    farmRepository.save(farm);
	
	}
	
	
	@Transactional
	public void buyNewChickens(int amount, double price, Long forFarmId) 
		   throws InsufficientMoneyException, 
		   		  NoEggsException, 
		   		  MaxStockException, 
		   		  FarmNotFoundException {
		
		Farm farm = this.getFarmById(forFarmId).orElseThrow(() -> new FarmNotFoundException(forFarmId));
		
		int stock = chickenService.getAllChickensByFarmOwnerId(forFarmId).size();
		
		if ( stock < stock + amount ) {
			throw new MaxStockException("chicken");
		}
		
		double money = farm.getMoney();
		double total_cost = price * amount;
		
		if (money < total_cost) {
			throw new InsufficientMoneyException();
		}
		
	    //Cattle and money amount update
	    this.addNewChickensForFarm(farm, amount);
	    farm.spendMoney(total_cost);
	    farmRepository.save(farm);

	}
	
	@Transactional
	public void sellEggs(int amount, Long fromFarmId, double paymentAmount) 
		   throws InsufficientStockException, 
		   		  NoEggsException, 
		   		  InsufficientPaymentException, 
		   		  FarmNotFoundException {
		
		Farm farm = this.getFarmById(fromFarmId).orElseThrow(() -> new FarmNotFoundException(fromFarmId));
	
		List<Egg> eggs = eggService.getAllEggsByFarmOwnerId(fromFarmId);
		int stock = eggs.size();
		
		if ( stock <  amount ) {
			throw new InsufficientStockException();
		}
		
		double total_cost =  eggs.get(0).getSellPrice() * amount;
		
		if (paymentAmount < total_cost) {
			throw new InsufficientPaymentException();
		}
		
	    //Cattle and money amount update
		List<Egg> soldEggs = eggs.subList(eggs.size() - amount, eggs.size());
	    eggs.removeAll(soldEggs);
	    farm.earnMoney(paymentAmount); 
	    farmRepository.save(farm);
	
	}	
	
	@Transactional
	public void sellChickens(int amount, Long fromFarmId, double paymentAmount) 
		   throws InsufficientStockException, 
		   		  NoEggsException, 
		   		  InsufficientPaymentException, 
		   		  FarmNotFoundException {
		
		Farm farm = this.getFarmById(fromFarmId).orElseThrow(() -> new FarmNotFoundException(fromFarmId));
	
		List<Chicken> chickens = chickenService.getAllChickensByFarmOwnerId(fromFarmId);
		int stock = chickens.size();
		
		if ( stock <  amount ) {
			throw new InsufficientStockException();
		}
		
		double total_cost =  chickens.get(0).getSellPrice() * amount;
		
		if (paymentAmount < total_cost) {
			throw new InsufficientPaymentException();
		}
		
	    //Cattle and money amount update
		List<Chicken> soldChickens = chickens.subList(chickens.size() - amount, chickens.size());
	    chickens.removeAll(soldChickens);
	    farm.earnMoney(paymentAmount); 
	    farmRepository.save(farm);
	
	}		
	
}
