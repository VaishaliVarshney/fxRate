package com.example.service.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.service.entity.FxRate;
import com.example.service.impl.FxRateServiceImpl;

@RestController
@RequestMapping("/fx")
public class FxRateController {

	@Autowired
	private final FxRateServiceImpl service;
	
	@Autowired
    public FxRateController(FxRateServiceImpl service) {
        this.service = service;
    }
	 @GetMapping("/{targetCurrency}")
	    public ResponseEntity<FxRate> getExchangeRates(@PathVariable String targetCurrency) {
	        FxRate rates = service.getExchangeRatesByTargetCurrency(targetCurrency);
	      
	        return ResponseEntity.ok(rates);
	    }
	 
	  @GetMapping
	    public ResponseEntity<List<FxRate>> getFxRates() {
	        System.out.println("Handling request for /fx");
	        List<FxRate> fxRates = service.getRatesForEurToSpecificCurrencies();
	        return new ResponseEntity<>(fxRates, HttpStatus.OK);
	    }
}
