package com.retooling.chickentestbackend.controller;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.retooling.chickentestbackend.dto.FarmRequestDTO;
import com.retooling.chickentestbackend.exceptions.farm.FarmNotFoundException;
import com.retooling.chickentestbackend.exceptions.farm.InsufficientMoneyException;
import com.retooling.chickentestbackend.exceptions.farm.InsufficientPaymentException;
import com.retooling.chickentestbackend.exceptions.farm.MaxStockException;
import com.retooling.chickentestbackend.exceptions.farm.NoChickensException;
import com.retooling.chickentestbackend.exceptions.farm.NoEggsException;
import com.retooling.chickentestbackend.model.Farm;
import com.retooling.chickentestbackend.services.FarmService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
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

	@SuppressWarnings("rawtypes")
	@PostMapping(value = "/buyEggs/eggAmount={eggAmount}/eggPrice={eggPrice}/farmId={farmId}")
	public ResponseEntity buyEggs(@PathVariable int eggAmount, double eggPrice, Long farmId) {
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
	@PostMapping(value = "/buyChickens/chickenAmount={chickenAmount}/chickenPrice={chickenPrice}/farmId={farmId}")
	public ResponseEntity buyChickens(@PathVariable int chickenAmount, double chickenPrice, Long farmId) {
		try {
			return ResponseEntity.ok(farmService.buyNewChickens(chickenAmount, chickenPrice, farmId));
		} catch (MaxStockException  |  InsufficientMoneyException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		} catch (FarmNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@SuppressWarnings("rawtypes")
	@PostMapping(value = "/sellEggs/eggAmount={eggAmount}/paymentAmount={payment}/farmId={farmId}")
	public ResponseEntity sellEggs(@PathVariable int eggAmount, double payment, Long farmId) {
		try {
			return ResponseEntity.ok(farmService.sellEggs(eggAmount, payment, farmId));
		} catch (InsufficientPaymentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		} catch (NoEggsException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

	@SuppressWarnings("rawtypes")
	@PostMapping(value = "/sellChickens/chickenAmount={amount}/paymentAmount={payment}/farmId={farmId}")
	public ResponseEntity sellChickens(@PathVariable int amount, double payment, Long farmId) {
		try {
			return ResponseEntity.ok(farmService.sellChickens(amount, payment, farmId));
		} catch (InsufficientPaymentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		} catch (NoChickensException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}	

}
