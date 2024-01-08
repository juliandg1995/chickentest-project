package com.retooling.chickentestbackend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

	@GetMapping("/home")
	public String showHomePage() {
		return "farm_main";
	}

//	@GetMapping("/getFarmSummaryForm")
//	public String getFarmSummaryForm(Model model) {
//		return "getFarmSummary";
//	}

}
