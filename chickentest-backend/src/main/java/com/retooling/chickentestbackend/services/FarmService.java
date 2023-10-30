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
	public Optional<Farm> getFarmById(Long farmId) {
		return farmRepository.findById(farmId);
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
	public boolean buyChickens(int amount, double price, Long farmId)
			throws InsufficientMoneyException, NoChickensException, MaxStockException, FarmNotFoundException {

		Farm farm = this.getFarmById(farmId).orElseThrow(() -> new FarmNotFoundException(farmId));

		List<Chicken> chickens = farm.getChickens();
		if (chickens.isEmpty()) {
			throw new NoChickensException();
		}

		// Max Stock Limit check
		int newStock = chickens.size() + amount;
		if (newStock > Farm.getMaxStockOfChickens()) {
			// Check allowed stock excess
			int excess = newStock - Farm.getMaxStockOfChickens();
			if (excess >= 20) {
				throw new MaxStockException("Chickens");
			}
			// If excess amount is allowed, discard the excess and set stock discount
			amount -= excess;
			// this.setDiscount(chickens); // El parámetro formal del tipo Product no admite
			// el tipo Chicken
		}

		double total = price * amount;

		// Insufficient money
		if (farm.getMoney() < total) {
			throw new InsufficientMoneyException();
		}

		// Cattle and money amount update
//	    farmRepository.addChickens(amount); // -> Tengo que crear los huevos en este service
//	    farmRepository.spendMoney(total);
		return true;
	}

	@SuppressWarnings("removal")
	@Transactional
	public void manageEclodedEgg(Egg anEclodedEgg) throws FarmNotFoundException, FailedOperationException {
		// Con el Cascade = ALL de las listas en Farm, se elimina automáticamente de BDD
		// al eliminar de lista
		Long farmOwnerId = anEclodedEgg.getfarmOwner().getId();
		if (farmOwnerId.equals(null)) {
			throw new FarmNotFoundException(new Long("1"));
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

//	@Transactional
//	public boolean buyEggs(int amount, double price) 
//		   throws InsufficientMoneyException, NoEggsException, MaxStockException {
//	    
//		List<Egg> eggs = farmRepository.getEggs()
//	            .orElseThrow(NoEggsException::new);
//		
//		// Max Stock Limit check
//		int newStock = eggs.size() + amount;
//		if ( newStock > Farm.getMaxStockOfEggs() ) {
//			// Check allowed stock excess
//			int excess = newStock - Farm.getMaxStockOfEggs();
//			if( excess >= 20 ) {
//			   throw new MaxStockException("Eggs");
//			}
//			// If excess amount is allowed, discard the excess and set stock discount 
//			amount -= excess;
////			this.setDiscount(eggs); 
//		}
//
//	    double total = price * amount;
//	    
//	    // Insufficient money
//	    if (farmRepository.getMoney() < total) {
//	        throw new InsufficientMoneyException();
//	    }
//	    
//	    //Cattle and money amount update
//	    farmRepository.addEggs(amount);
//	    farmRepository.spendMoney(total);
//	    return true;
//	}
//	
//	@Transactional
//	public boolean sellChickens(int amount, double payment) 
//		   throws InsufficientStockException, NoChickensException, InsufficientPaymentException {
//	    
//		// Get the list of hatched 
//		List<Chicken> chickens = farmRepository.getChickens()
//	            .orElseThrow(NoChickensException::new);
//		
//		if ( chickens.size() < amount ) {
//			throw new InsufficientStockException();
//		}
//
//	    double total = chickens.stream()
//	            .findAny()
//	            .map(Chicken::getSellPrice)
//	            .orElseThrow(NoChickensException::new) * amount;
//
//	    if ( payment < total ) {
//	        throw new InsufficientPaymentException();
//	    }
//
//	    farmRepository.deleteChickens(amount);
//	    farmRepository.earnMoney(total);
//	    return true;
//	}
//	
//	@Transactional
//	public boolean sellEggs(int amount, double payment) 
//		   throws InsufficientStockException, NoEggsException, InsufficientPaymentException {
//	    
//		// Get the list of hatched eggs
//		List<Egg> eggs = this.getNonHatchedEggs();
//		
//	    if (eggs.isEmpty()) {
//	        throw new NoEggsException();
//	    }
//		
//		if ( eggs.size() < amount ) {
//			throw new InsufficientStockException();
//		}
//
//	    double total = eggs.stream()
//	            .findAny()
//	            .map(Egg::getSellPrice)
//	            .orElseThrow(NoEggsException::new) * amount;
//
//	    if ( payment < total ) {
//	        throw new InsufficientPaymentException();
//	    }
//
//	    farmRepository.deleteEggs(amount);
//	    farmRepository.earnMoney(total);
//	    return true;
//	}
//	
//	public void setDiscount(List<Product> products) {
//
////		 products.stream().filter(x -> x.isDiscountMaterial())
////		 .limit(100)
////		 .map(Product p -> p.setDiscount());
//		
//		List<Product> forDiscount = products.stream().filter(x -> x.isDiscountMaterial())
//						 							 .limit(100)
//						 							 .collect(Collectors.toList());
//		for( Product product:forDiscount ) {
//			product.setDiscount();
//		}
//	}	
	
}
