package com.retooling.chickentestbackend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.retooling.chickentestbackend.dto.FarmRequestDTO;

@Controller
public class ViewController {

	@GetMapping("/home")
	public String showHomePage(Model model) {
		model.addAttribute("farmRequest", new FarmRequestDTO());
		return "farm_main";
	}

	@GetMapping("/getFarmSummaryForm")
	public String getFarmSummaryForm(Model model) {
		return "farmSummary";
	}

}
