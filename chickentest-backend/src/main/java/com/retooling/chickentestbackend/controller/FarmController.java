package com.retooling.chickentestbackend.controller;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.retooling.chickentestbackend.services.FarmService;

import jakarta.persistence.EntityNotFoundException;

import com.retooling.chickentestbackend.repository.*;
import com.retooling.chickentestbackend.model.*;
import com.retooling.chickentestbackend.exceptions.*;
import com.retooling.chickentestbackend.exceptions.farm.*;

@RestController
@RequestMapping("/farms")
public class FarmController {

	@Autowired
	private FarmService farmService;

//	@PostMapping(value = "/createFarm", produces = MediaType.APPLICATION_JSON_VALUE)
//	public ResponseEntity<Farm> createFarm(@RequestBody Farm farm) throws CannotCreateFarmException {
//		try {
//			return ResponseEntity.ok(farmService.createFarm(farm));
//		} catch(CannotCreateFarmException e){
//			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
//		}
//	}
	// To get ALL farms
	// Probé este método en Postman. El farmService devuelve las granjas, pero de todas maneras arroja una excepción
	// El error dice que no puede armar el JSON
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
	// Mismo caso que con el anterior. El servicio trae la granja correcta, pero tira excpción igual
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
        } catch(NoFarmFoundException e) {
        	return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage() + "ID");
        }
    }
//   
//   @GetMapping("/{id}/money")
//   public ResponseEntity<Double> getFarmMoneyById(@PathVariable Long id) {
//       Double money = farmService.getMoneyById(id);
//       if (money != null) {
//           return ResponseEntity.ok(money);
//       } else {
//           return ResponseEntity.notFound().build();
//       }
//   }   

//	@PostMapping(value = "/buyEggs", produces = MediaType.APPLICATION_JSON_VALUE)
//	public ResponseEntity buyEggs(@PathVariable int eggAmount, double eggPrice) {
//		try {
//			return ResponseEntity.ok(farmService.buyEggs(eggAmount, eggPrice));
//		} catch (InsufficientMoneyException | MaxStockException e) {
//			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
//		} catch (NoEggsException e) {
//			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
//		} catch (Exception e) {
//			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
//		}
//	}
//
//	@PostMapping(value = "/buyChickens", produces = MediaType.APPLICATION_JSON_VALUE)
//	public ResponseEntity buyChickens(@PathVariable int chickenAmount, double chickenPrice) {
//		try {
//			return ResponseEntity.ok(farmService.buyChickens(chickenAmount, chickenPrice));
//		} catch (MaxStockException  |  InsufficientMoneyException e) {
//			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
//		} catch (NoChickensException e) {
//			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
//		} catch (Exception e) {
//			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
//		}
//	}
//	
//	@PostMapping(value = "/sellEggs", produces = MediaType.APPLICATION_JSON_VALUE)
//	public ResponseEntity sellEggs(@PathVariable int eggAmount, double payment) {
//		try {
//			return ResponseEntity.ok(farmService.sellEggs(eggAmount, payment));
//		} catch (InsufficientPaymentException e) {
//			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
//		} catch (NoEggsException e) {
//			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
//		} catch (Exception e) {
//			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
//		}
//	}
//	
//	@PostMapping(value = "/sellChickens", produces = MediaType.APPLICATION_JSON_VALUE)
//	public ResponseEntity sellChickens(@PathVariable int chickenAmount, double payment) {
//		try {
//			return ResponseEntity.ok(farmService.sellChickens(chickenAmount, payment));
//		} catch (InsufficientPaymentException e) {
//			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
//		} catch (NoChickensException e) {
//			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
//		} catch (Exception e) {
//			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
//		}
//	}
	
	

}
