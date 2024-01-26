package com.retooling.chickentestbackend.controller;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.retooling.chickentestbackend.dto.FarmRequestDTO;
import com.retooling.chickentestbackend.exceptions.farm.FarmNotFoundException;
import com.retooling.chickentestbackend.exceptions.farm.InsufficientMoneyException;
import com.retooling.chickentestbackend.exceptions.farm.InsufficientPaymentException;
import com.retooling.chickentestbackend.exceptions.farm.MaxStockException;
import com.retooling.chickentestbackend.exceptions.farm.NegativeValuesException;
import com.retooling.chickentestbackend.exceptions.farm.NoChickensException;
import com.retooling.chickentestbackend.exceptions.farm.NoEggsException;
import com.retooling.chickentestbackend.model.Chicken;
import com.retooling.chickentestbackend.model.Farm;
import com.retooling.chickentestbackend.services.FarmService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

//@RestController
@Controller
@RequestMapping("/farms")
public class FarmController {

	@Autowired
	private FarmService farmService;

	@Transactional
	@PostMapping(value = "/createFarm", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Farm> createFarm(@Valid @RequestBody FarmRequestDTO farmRequest) {
		try {
			return ResponseEntity.ok(farmService.createFarm(farmRequest.getName(), farmRequest.getMoney()));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	@Transactional
	@DeleteMapping(value = "/deleteFarm/{farmId}")
	public ResponseEntity<?> deleteFarm(@PathVariable Long farmId) {
		try {
			farmService.deleteFarmById(farmId);
			return new ResponseEntity<>("Farm with ID " + farmId + " deleted successfully", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Failed to delete farm with ID " + farmId, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// To get ALL farms
	@GetMapping(value = "/getFarms")
	public ResponseEntity<List<Farm>> getFarms() {
		try {
			List<Farm> farms = farmService.getAllFarms();
			return ResponseEntity.ok(farms);
		} catch (EntityNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
	}

	// To get Farm searching BY FARM ID
	@GetMapping(value = "/getFarmById/{farmOwnerId}")
	public ResponseEntity<Farm> getFarmById(@PathVariable Long farmOwnerId) {
		try {
			Optional<Farm> farmOptional = farmService.getFarmById(farmOwnerId);
			Farm farm = farmOptional.get();
			return ResponseEntity.ok(farm);
		} catch (EntityNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
	}

	/// For API testing:
	@GetMapping(value = "getFarm/{id}/summary")
	public ResponseEntity<String> getFarmSummary(@PathVariable Long id) {
		try {
			String farmSummary = farmService.getFarmSummaryById(id);
			return ResponseEntity.ok(farmSummary);
		} catch (FarmNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage() + id);
		}
	}

	@GetMapping("/{id}/money")
	public ResponseEntity<Double> getFarmMoneyById(@PathVariable Long id) {
		Double money = farmService.getMoneyById(id);
		if (money != null) {
			return ResponseEntity.ok(money);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	// For API testing:
	@PostMapping(value = "/passDays/{numberOfDays}")
	public ResponseEntity<String> passDays(@PathVariable int numberOfDays) {
		try {
			farmService.passDays(numberOfDays);
			return ResponseEntity.ok(numberOfDays + " have passed successfully");
		} catch (InvalidParameterException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An internal error occurred");
		}
	}

	// Ejemplo request:
	// localhost:8080/farms/buyNewEggs?eggAmount=2&eggPrice=10&farmId=1
	@PostMapping(value = "/buyNewEggs")
	@SuppressWarnings("rawtypes")
	public ResponseEntity buyEggs(@RequestParam int eggAmount, @RequestParam double eggPrice,
			@RequestParam Long farmId) {
		try {
			return ResponseEntity.ok(farmService.buyNewEggs(eggAmount, eggPrice, farmId));
		} catch (InsufficientMoneyException | MaxStockException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		} catch (FarmNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

	@SuppressWarnings("rawtypes")
	@PostMapping(value = "/buyNewChickens")
	public ResponseEntity buyChickens(@RequestParam int chickenAmount, @RequestParam double chickenPrice,
			@RequestParam Long farmId) {
		try {
			return ResponseEntity.ok(farmService.buyNewChickens(chickenAmount, chickenPrice, farmId));
		} catch (MaxStockException | InsufficientMoneyException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		} catch (FarmNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

	@SuppressWarnings("rawtypes")
	@PostMapping(value = "/sellEggs")
	public ResponseEntity sellEggs(@RequestParam int eggAmount, @RequestParam double payment,
			@RequestParam Long farmId) {
		try {
			return ResponseEntity.ok(farmService.sellEggs(eggAmount, payment, farmId));
		} catch (InsufficientPaymentException | NegativeValuesException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		} catch (NoEggsException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

	@SuppressWarnings("rawtypes")
	@PostMapping(value = "/sellChickens")
	public ResponseEntity sellChickens(@RequestParam int amount, @RequestParam double payment,
			@RequestParam Long farmId) {
		try {
			return ResponseEntity.ok(farmService.sellChickens(amount, payment, farmId));
		} catch (InsufficientPaymentException | NegativeValuesException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		} catch (NoChickensException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

	////////////////////////////////////////////////
	///// 			FORM CONTROLLERS 		   /////
	////////////////////////////////////////////////
	
	@GetMapping("/getAllFarmsForm")
    public String getAllFarmsForm(Model model) {
        List<Farm> farms = farmService.getAllFarms();
        model.addAttribute("farms", farms);
        return "farmList";  
    }

	@GetMapping("/getFarmSummaryForm")
	public String getFarmSummaryForm(@RequestParam(name = "farmId") Long farmId, Model model) {
		try {
			// Here it retrieves the farm summary from the service first, then sends it to
			// the form
			String farmSummary = farmService.getFarmSummaryById(farmId);
			model.addAttribute("farmSummary", farmSummary);
			return "farmSummary";
		} catch (FarmNotFoundException e) {
			model.addAttribute("farmSummary", e.getMessage() + farmId);
			return "farmSummary";
		}
	}

	@PostMapping(value = "/getPassDaysForm")
	public String passDaysForm(@RequestParam(name = "numberOfDays") int numberOfDays, Model model) {
		try {
			farmService.passDays(numberOfDays);
			String response = numberOfDays + " days have passed successfully";
			model.addAttribute("response", response);
			List<Chicken> chickens = farmService.getFarmById(Long.valueOf(1)).get().getChickens();
			return "passDaysFormResponse";
		} catch (InvalidParameterException e) {
			String response = e.getMessage();
			model.addAttribute("response", response);
			return "passDaysFormRespose"; 
		} catch (Exception e) {
			String response = e.getMessage();
			model.addAttribute("response", response);
			return "passDaysFormResponse";
		}
	}

	@PostMapping(value = "/createFarmForm")
	public ResponseEntity<String> createFarmForm(@Valid @ModelAttribute FarmRequestDTO farmRequest, Model model) {
		try {
			farmService.createFarm(farmRequest.getName(), farmRequest.getMoney());
			return ResponseEntity.ok("Farm created successfully");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create farm");
		}
	}
	
	@PostMapping(value = "/deleteFarmForm", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> deleteFarmForm(@RequestParam(name = "farmId") Long farmId, Model model) {
	    try {
	        farmService.deleteFarmById(farmId);
	        return ResponseEntity.ok("Farm deleted successfully");
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete farm");
	    }
	}	

	@PostMapping(value = "/buyEggsForm")
	public String buyEggsForm(@RequestParam int eggAmount, @RequestParam double eggPrice, @RequestParam Long farmId,
			Model model) {
		try {
			String buyEggsResponse = farmService.buyNewEggs(eggAmount, eggPrice, farmId);
			model.addAttribute("response", buyEggsResponse);
			return "buyProducts";
		} catch (NegativeValuesException | InsufficientMoneyException | MaxStockException e) {
			model.addAttribute("response", e.getMessage());
			return "buyProducts";
		} catch (FarmNotFoundException e) {
			model.addAttribute("response", e.getMessage());
			return "buyProducts";
		} catch (Exception e) {
			model.addAttribute("buyEggsResponse", e.getMessage());
			return "buyProducts";
		}
	}

	@PostMapping(value = "/buyChickensForm")
	public String buyChickensForm(@RequestParam int chickenAmount, @RequestParam double chickenPrice, @RequestParam Long farmId,
			Model model) {
		try {
			String buyChickensResponse = farmService.buyNewChickens(chickenAmount, chickenPrice, farmId);
			model.addAttribute("response", buyChickensResponse);
			return "buyProducts";
		} catch (NegativeValuesException | InsufficientMoneyException | MaxStockException e) {
			model.addAttribute("response", e.getMessage());
			return "buyProducts";
		} catch (FarmNotFoundException e) {
			model.addAttribute("response", e.getMessage());
			return "buyProducts";
		} catch (Exception e) {
			model.addAttribute("response", e.getMessage());
			return "buyProducts";
		}
	}

	@PostMapping(value = "/sellEggsForm")
	public String sellEggsForm(@RequestParam int eggAmount, @RequestParam double payment,
	                            @RequestParam Long farmId, Model model) {
	    try {
	        String sellEggsResponse = farmService.sellEggs(eggAmount, payment, farmId);
	        model.addAttribute("response", sellEggsResponse);
	        return "sellProducts";
	    } catch (InsufficientPaymentException | NegativeValuesException e) {
	        model.addAttribute("response", e.getMessage());
	        return "sellProducts";
	    } catch (NoEggsException e) {
	        model.addAttribute("response", e.getMessage());
	        return "sellProducts";
	    } catch (Exception e) {
	        model.addAttribute("response", e.getMessage());
	        return "sellProducts";
	    }
	}


	@PostMapping(value = "/sellChickensForm")
	public String sellChickensForm(@RequestParam int chickenAmount, @RequestParam double payment,
	                               @RequestParam Long farmId, Model model) {
	    try {
	        String sellChickensResponse = farmService.sellChickens(chickenAmount, payment, farmId);
	        model.addAttribute("response", sellChickensResponse);
	        return "sellProducts";
	    } catch (InsufficientPaymentException | NegativeValuesException e) {
	        model.addAttribute("response", e.getMessage());
	        return "sellProducts";
	    } catch (NoChickensException e) {
	        model.addAttribute("response", e.getMessage());
	        return "sellProducts";
	    } catch (Exception e) {
	        model.addAttribute("response", e.getMessage());
	        return "sellProducts";
	    }
	}

}
