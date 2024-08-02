package com.example.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.service.entity.FxRate;
import com.example.service.model.FxRateApiResponse;
import com.example.service.repository.FxRateRepository;

@Service
public class FxRateServiceImpl {
	private final FxRateRepository repository;

	@Autowired
	public FxRateServiceImpl(FxRateRepository repository) {
		this.repository = repository;
	}

	private RestTemplate restTemplate = new RestTemplate();

	@Value("${fx.api.url}")
	private String apiUrl;

	// conversion of particular currency
	public FxRate getExchangeRatesByTargetCurrency(String targetCurrency) {
		FxRate response = repository.findByTargetCurrency(targetCurrency);
		if (response != null)
			return response;
		else {
			FxRate fxRate = fetchRateFromExternalApi(targetCurrency);
//			repository.save(fxRate);
			return fxRate;
		}

	}
//connect to external api
	private FxRate fetchRateFromExternalApi(String targetCurrency) {
		String url = apiUrl + "?to=" + targetCurrency;
		FxRateApiResponse response = restTemplate.getForObject(url, FxRateApiResponse.class);
		FxRate fxRate = new FxRate();
		fxRate.setSourceCurrency("EUR");
		fxRate.setTargetCurrency(targetCurrency);
		fxRate.setExchangeRate(response.getRates().get(targetCurrency));
		fxRate.setDate("2024-03-18");
		return fxRate;
	}

	public List<FxRate> getRatesForEurToSpecificCurrencies() {
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(apiUrl).queryParam("base", "EUR")
				.queryParam("symbols", "USD,GBP,JPY,CZK");
		String apiUrl = builder.toUriString();
		FxRateApiResponse response = restTemplate.getForObject(apiUrl, FxRateApiResponse.class);
		List<FxRate> fxRates = new ArrayList<>();
		if (response != null && response.getRates() != null) {
			for (Map.Entry<String, Double> entry : response.getRates().entrySet()) {
				FxRate fxRate = new FxRate();
				fxRate.setSourceCurrency(response.getBase());
				fxRate.setTargetCurrency(entry.getKey());
				fxRate.setExchangeRate(entry.getValue());
				fxRate.setDate("2024-03-18");
				fxRates.add(fxRate);
			}
		}
		return fxRates;
	}
}
