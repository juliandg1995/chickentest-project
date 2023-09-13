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

import com.retooling.chickentestbackend.model.Farm;
import com.retooling.chickentestbackend.services.FarmService;

import jakarta.persistence.EntityNotFoundException;

import com.retooling.chickentestbackend.repository.*;
import com.retooling.chickentestbackend.model.*;
import com.retooling.chickentestbackend.exceptions.*;
import com.retooling.chickentestbackend.exceptions.farm.*;

@RestController
@RequestMapping("chickentestbackend/farms")
public class FarmController {

	@Autowired
	private FarmService farmService;

//	@PostMapping(value = "/createFarm", produces = MediaType.APPLICATION_JSON_VALUE)
//	public ResponseEntity<Farm> createFarm(@RequestBody Farm farm) {
//
//		return ResponseEntity.ok(farmService.createFarm(farm));
//	}
	
//   @GetMapping("/{id}/summary")
//    public ResponseEntity<String> getFarmSummary(@PathVariable Long id) {
//        String farmSummary = farmService.getFarmSummaryById(id);
//        if (farmSummary.equals("Farm not found")) {
//            return ResponseEntity.notFound().build();
//        }
//        return ResponseEntity.ok(farmSummary);
//    }
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
