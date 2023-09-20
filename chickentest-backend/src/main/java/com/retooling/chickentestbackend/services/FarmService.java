package com.retooling.chickentestbackend.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.retooling.chickentestbackend.model.Farm;
import com.retooling.chickentestbackend.repository.FarmRepository;
import com.retooling.chickentestbackend.services.ChickenService;
import com.retooling.chickentestbackend.services.EggService;


import jakarta.transaction.Transactional; // -> Investigar qué hace y cuándo se usa

@Service
public class FarmService {
	
	//// Dependencies
	
	@Autowired
	private FarmRepository farmRepository;
	
//	@Autowired	
//	private ChickenService chickenService;
	
//	@Autowired 
//	private EggService eggService;
	
	////  Methods
	
//	@Transactional
//	public Farm createFarm(Farm farm) {
//		return farmRepository.save(farm);
//	}
//	
//    public List<Farm> getAllFarms() {
//        return (List<Farm>) farmRepository.findAll();
//    }
//    
    // Returns a farm, searching by ID
    public Optional<Farm> getFarmById(Long farmId) {
        return farmRepository.findById(farmId);
    }	
<<<<<<< HEAD
//    
=======
    
>>>>>>> feature/createEgg
//    // Returns all information in string format
//    public String getFarmSummaryById(Long farmId) {
//        Optional<Farm> farmOptional = this.getFarmById(farmId);
//        if (farmOptional.isPresent()) {
//            Farm farm = farmOptional.get();
//            int eggCount = countEggsInFarm(farm);
//            int chickenCount = countChickensInFarm(farm);
//
//            return "Farm ID: " + farm.getId() + "\n" +
//                    "Farm Name: " + farm.getName() + "\n" +
//                    "Egg Count: " + eggCount + "\n" +
//                    "Chicken Count: " + chickenCount + "\n" +
//                    "Money: " + farm.getMoney();
//        } else {
//            return "Farm not found";
//        }
//    }
//    
//    private int countEggsInFarm(Farm farm) {
//        return farm.getEggs().size();
//    }
//
//    private int countChickensInFarm(Farm farm) {
//        return farm.getChickens().size();
//    }
//    
//    public Double getMoneyById(Long farmId) {
//    	 Optional<Double> moneyOptional = farmRepository.findMoneyById(farmId);
//         
//         return moneyOptional.orElse(null);
//     }
//    
//    public String getNameById(Long farmId) {
//   	 Optional<String> nameOptional = farmRepository.findNameById(farmId);
//        
//        return nameOptional.orElse(null);
//    }    
//	
//	public List<Egg> getEggs() {
//
//		return farmRepository.getEggs()
//				.orElseThrow( () -> new EntityNotFoundException("La granja no tiene huevos"));
//	}
//	
//	public List<Egg> getHatchedEggs() {
//		
//		List<Egg> hatchedEggs = farmRepository.
//								getEggs().
//								get().stream().
//								filter(egg -> egg.isHatched()).
//								collect(Collectors.toList());
//		return hatchedEggs;
//		
//	}
//	
//	
//	public List<Egg> getNonHatchedEggs() {
//		
//		List<Egg> notHatchedEggs = farmRepository.
//								getEggs().
//								get().stream().
//								filter(egg -> !egg.isHatched()).
//								collect(Collectors.toList());
//		return notHatchedEggs;
//		
//	}
//	
//	public List<Chicken> getChickens() {
//
//		return farmRepository.getChickens()
//				.orElseThrow( () -> new EntityNotFoundException("La granja no tiene gallinas"));
//	}
//	
//	@Transactional
//	public boolean buyChickens(int amount, double price) 
//		   throws InsufficientMoneyException, NoChickensException, MaxStockException {
//	    
//		List<Chicken> chickens = farmRepository.getChickens()
//	            .orElseThrow(NoChickensException::new);
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
//	    if (farmRepository.getMoney() < total) {
//	        throw new InsufficientMoneyException();
//	    }
//	    
//	    //Cattle and money amount update
//	    farmRepository.addChickens(amount);
//	    farmRepository.spendMoney(total);
//	    return true;
//	}
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
