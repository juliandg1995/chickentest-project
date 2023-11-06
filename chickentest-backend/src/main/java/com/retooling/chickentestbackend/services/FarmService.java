package com.retooling.chickentestbackend.services;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.Optional;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

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


	@Transactional
	public void manageEclodedEgg(Egg anEclodedEgg) throws FarmNotFoundException, FailedOperationException, 
														  MaxStockException, InsufficientStockException, 
														  NoChickensException, InsufficientPaymentException {
		// Con el Cascade = ALL de las listas en Farm, se elimina automáticamente de BDD
		// al eliminar de lista

		double chickenPrice;
		Farm farmOwner = anEclodedEgg.getfarmOwner();
		if (farmOwner.equals(null)) {
			throw new FarmNotFoundException(0L);
		}
		Long farmOwnerId = farmOwner.getId();
		// Stock control
		List<Chicken> chickens = farmOwner.getChickens();
		
		if (!chickens.isEmpty()) {
			chickenPrice = chickens.get(0).getSellPrice();
		} else {
			chickenPrice = Chicken.getDefaultSellPrice();
		}
		
		// If max stock is reached, a chicken will be sold at discount
		if (chickens.size() == Farm.getMaxStockOfChickens()) {
//			throw new MaxStockException("Egg");
			this.sellChickens(1, chickenPrice, farmOwnerId);
		}
		
		List<Egg> eggs = farmOwner.getEggs();
		if (eggs.contains(anEclodedEgg)) {
			eggs.remove(anEclodedEgg);
			eggService.deleteEgg(anEclodedEgg.getId());
		}
		
//		this.removeEggFromList(farmOwnerId, anEclodedEgg);  -> Decidir si mantener el método, refactorizarlo o eliminarlo
		Chicken newChicken = chickenService.createChicken(chickenPrice, 0, farmOwnerId);
		this.addChickenToFarmList(newChicken, farmOwnerId);
		
	}

	@Transactional
	public String manageNewEggs(Long farmId) throws FailedOperationException, FarmNotFoundException, InsufficientStockException, NoEggsException, InsufficientPaymentException {
		try {
			double eggPrice;
			Farm farm = farmRepository.findById(farmId).get();
			List<Egg> eggs = farm.getEggs();
			
			if (!eggs.isEmpty()) {
				eggPrice = eggs.get(0).getSellPrice();
			} else {
				eggPrice = Chicken.getDefaultSellPrice();
			}
			
			// Stock Control
			if (eggs.size() + Chicken.getEggAmount() > Farm.getMaxStockOfEggs()) {
				int excess = (eggs.size() + Chicken.getEggAmount()) - Farm.getMaxStockOfEggs();
			    sellEggs(excess, eggPrice, farm.getId());
			}
			
			for (int i = 0; i < Chicken.getEggAmount(); i++) {
				eggService.createEgg(eggPrice, farmId);
			}
			return "OK";
		} catch (EntityNotFoundException e) {
			return e.getMessage();
		}
	}

	public void passDays(int numberOfDays) throws FailedOperationException, InvalidParameterException, IterationException, MaxStockException {
		// For Eggs
		if (numberOfDays < 1) {
			throw new InvalidParameterException();
		}
		
		List<Chicken> actualChickens = chickenService.getAllChickens();
		eggService.passDays(numberOfDays);
		List<Egg> newEggs = chickenService.passDays(numberOfDays, actualChickens);
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
	public String buyNewEggs(int amount, double price, Long forFarmId) 
		   throws InsufficientMoneyException, 
		   		  MaxStockException, 
		   		  FarmNotFoundException {
		
		Farm farm = this.getFarmById(forFarmId).orElseThrow(() -> new FarmNotFoundException(forFarmId));
		
		int currentStock = eggService.getAllEggsByFarmOwnerId(forFarmId).size();
		
		if ( Farm.getMaxStockOfEggs() < currentStock + amount ) {
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
	    
	    return amount + " eggs have been bought by " + farm.getName();
	
	}
	
	
	@Transactional
	public String buyNewChickens(int amount, double price, Long forFarmId) 
		   throws InsufficientMoneyException, 
		   		  MaxStockException, 
		   		  FarmNotFoundException {
		
		Farm farm = this.getFarmById(forFarmId).orElseThrow(() -> new FarmNotFoundException(forFarmId));
		
		int currentStock = chickenService.getAllChickensByFarmOwnerId(forFarmId).size();
		
		if ( Farm.getMaxStockOfChickens() < currentStock + amount ) {
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

	    return amount + " chickens have been bought by " + farm.getName();
	}
	
	@Transactional
	public String sellEggs(int amount, double paymentAmount, Long fromFarmId) 
		   throws InsufficientStockException, 
		   		  NoEggsException, 
		   		  InsufficientPaymentException, 
		   		  FarmNotFoundException {
		
		Farm farm = this.getFarmById(fromFarmId).orElseThrow(() -> new FarmNotFoundException(fromFarmId));
	
		List<Egg> eggs = farm.getEggs();
		int currentStock = eggs.size();
		
		if ( currentStock <  amount ) {
			throw new InsufficientStockException();
		}
		
		double totalCost =  eggs.get(0).getSellPrice() * amount;
		
		if (paymentAmount < totalCost) {
			throw new InsufficientPaymentException();
		}
		
	    //Cattle and money amount update
		List<Egg> soldEggs = eggs.subList(eggs.size() - amount, eggs.size());
		soldEggs.stream().forEach(e-> eggService.deleteEgg(e.getId()));
	    eggs.removeAll(soldEggs);
	    farm.earnMoney(totalCost); 
	    farmRepository.save(farm);
	    
	    return amount + " eggs have been sold by " + farm.getName();
	
	}	
	
	@Transactional
	public String sellChickens(int amount, double paymentAmount, Long fromFarmId) 
		   throws InsufficientStockException, 
		   		  NoChickensException, 
		   		  InsufficientPaymentException, 
		   		  FarmNotFoundException {
		
		Farm farm = this.getFarmById(fromFarmId).orElseThrow(() -> new FarmNotFoundException(fromFarmId));
	
		List<Chicken> chickens = farm.getChickens();
		int currentStock = chickens.size();
		
		if ( currentStock <  amount ) {
			throw new InsufficientStockException();
		}
		
		double totalCost =  chickens.get(0).getSellPrice() * amount;
		
		if (paymentAmount < totalCost) {
			throw new InsufficientPaymentException();
		}
		
	    //Cattle and money amount update
		List<Chicken> soldChickens = chickens.subList(chickens.size() - amount, chickens.size());
		soldChickens.stream().forEach(c-> chickenService.deleteChicken(c.getId()));
	    chickens.removeAll(soldChickens);
	    farm.earnMoney(totalCost); 
	    farmRepository.save(farm);
	    
	    return amount + " chickens have been sold by " + farm.getName();
	
	}		
	
}
