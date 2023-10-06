package com.retooling.chickentestbackend.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.retooling.chickentestbackend.exceptions.farm.EggNotFoundException;
import com.retooling.chickentestbackend.exceptions.farm.FarmNotFoundException;
import com.retooling.chickentestbackend.exceptions.farm.InsufficientMoneyException;
import com.retooling.chickentestbackend.exceptions.farm.MaxStockException;
import com.retooling.chickentestbackend.exceptions.farm.NoChickensException;
import com.retooling.chickentestbackend.exceptions.farm.NoFarmFoundException;
import com.retooling.chickentestbackend.model.Chicken;
import com.retooling.chickentestbackend.model.Egg;
import com.retooling.chickentestbackend.model.Farm;
import com.retooling.chickentestbackend.repository.FarmRepository;

import jakarta.transaction.Transactional; // -> Investigar qué hace y cuándo se usa

@Service
public class FarmService {
	
	//// Dependencies
	
	@Autowired
	private FarmRepository farmRepository;

//	@Autowired	
//	private ChickenService chickenService;
//	
//	@Autowired 
//	private EggService eggService;
	
	////  Methods
	
	@Transactional
	public Farm createFarm(String name, double money) {
		try {
			Farm newFarm = new Farm(name, money);
			return farmRepository.save(newFarm);
		} catch(Exception e) {
			new Exception();
			return null;
		}
	}
	
    public List<Farm> getAllFarms() {
        return (List<Farm>) farmRepository.findAll();
    }
    
    // Returns a farm, searching by ID
    public Optional<Farm> getFarmById(Long farmId) {
        return farmRepository.findById(farmId);
    }	
    
    public String getFarmSummaryById(Long farmId) throws NoFarmFoundException  {
    	
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
            farmSummary += "Id: " + farmIdCode +"\n";
            farmSummary += "Money: " + farmMoney + "\n";
            farmSummary += "Number of Chickens: " + chickenCount + "\n";
            farmSummary += "Number of Eggs: " + eggCount + "\n";

            return farmSummary;
        } else {
            // If not found, throw exception with farm's missing information ("ID")
            throw new NoFarmFoundException("ID");
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


//	@Transactional
//	public boolean buyChickens(int amount, double price, Long farmId) 
//		   throws InsufficientMoneyException, NoChickensException, MaxStockException, NoFarmFoundException {
//		
//        Farm farm = this.getFarmById(farmId).orElseThrow(() -> new NoFarmFoundException("ID"));
//	    
//		List<Chicken> chickens = farm.getChickens();
//		if (chickens.isEmpty()) {
//		    throw new NoChickensException();
//		}
//		
//		// Max Stock Limit check
//		int newStock = chickens.size() + amount;
//		if ( newStock > Farm.getMaxStockOfChickens() ) {
//			// Check allowed stock excess
//			int excess = newStock - Farm.getMaxStockOfChickens();
//			if( excess >= 20 ) {
//			   throw new MaxStockException("Chickens");
//			}
//			// If excess amount is allowed, discard the excess and set stock discount 
//			amount -= excess;
//		//	this.setDiscount(chickens);  // El parámetro formal del tipo Product no admite el tipo Chicken
//		}
//
//	    double total = price * amount;
//	    
//	    // Insufficient money
//	    if (farm.getMoney() < total) {
//	        throw new InsufficientMoneyException();
//	    }
//	    
//	    //Cattle and money amount update
////	    farmRepository.addChickens(amount); // -> Tengo que crear los huevos en este service
////	    farmRepository.spendMoney(total);
//	    return true;
//	}
//	
//	public void removeEggFromList(Long farmId, Egg eggToRemove) throws EggNotFoundException, FarmNotFoundException {
//        Optional<Farm> optionalFarm = farmRepository.findById(farmId);
//        if (optionalFarm.isPresent()) {
//            Farm farm = optionalFarm.get();
//            List<Egg> eggs = farm.getEggs();
//            boolean removed = eggs.remove(eggToRemove); // Remove the specific egg object from list
//            if (!removed) {
//                throw new EggNotFoundException(eggToRemove.getId());
//            }
//            farmRepository.save(farm); // Save the updated Farm object
//        } else {
//            throw new FarmNotFoundException(farmId);
//        }
//	}
	
//	public String passDays(int numberOfDays) {
//		// For Eggs
//		// Falta agregar las excepciones
////		eggService.passDays(numberOfDays);
////		chickenService.passDays(numberOfDays);		
//		return "OK";
//	}
//	
//	
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
//	
//	public void passADay() {
//		
//	}

}
