package com.example.service.model;

import java.util.Map;

import lombok.Data;

@Data
public class FxRateApiResponse {
	private String base;
    private String date;
    private Map<String, Double> rates;
}
