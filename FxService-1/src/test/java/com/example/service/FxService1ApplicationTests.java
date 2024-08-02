package com.example.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import com.example.service.entity.FxRate;
import com.example.service.impl.FxRateServiceImpl;
import com.example.service.model.FxRateApiResponse;
import com.example.service.repository.FxRateRepository;

@SpringBootTest
class FxService1ApplicationTests {

	@Mock
	private FxRateRepository repository;

	private RestTemplate restTemplate = new RestTemplate();
	
	@InjectMocks
	private FxRateServiceImpl fxRateService;
	@Value("${fx.api.url}")
	private String apiUrl;

	@BeforeEach
	public void setUp()
			throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		MockitoAnnotations.openMocks(this);
		/*
		 * Field apiUrlField = FxRateServiceImpl.class.getDeclaredField("apiUrl");
		 * apiUrlField.setAccessible(true); apiUrlField.set(fxRateService,
		 * "https://api.frankfurter.app/latest");
		 */    ReflectionTestUtils.setField(fxRateService, "apiUrl", "https://api.frankfurter.app/latest");
	     
	}
	

	@Test
	public void testGetExchangeRatesByTargetCurrency_FromDB() {
		FxRate fxRate = new FxRate(1L, "EUR", "USD", 1.0789, "2024-07-26");
		when(repository.findByTargetCurrency("USD")).thenReturn(fxRate);

		FxRate result = fxRateService.getExchangeRatesByTargetCurrency("USD");

		assertEquals("EUR", result.getSourceCurrency());
		assertEquals("USD", result.getTargetCurrency());
		assertEquals(1.0789, result.getExchangeRate());
		assertEquals("2024-03-18", result.getDate());
	}

    @Test
    public void testGetRatesForEurToSpecificCurrencies() {
        FxRateApiResponse response = new FxRateApiResponse();
        response.setBase("EUR");
        response.setDate(LocalDate.now().toString());
        Map<String, Double> rates = new HashMap<>();
        rates.put("USD",1.0789);
        rates.put("GBP", 0.84328);
        rates.put("JPY",162.66);
        rates.put("CZK",25.454);
        response.setRates(rates);
        // Mock the RestTemplate response
    //    String url = "https://api.frankfurter.app/latest?base=EUR&symbols=USD,GBP,JPY,CZK";
     //   when(restTemplate.getForObject(eq(url), eq(FxRateApiResponse.class))).thenReturn(response);


        List<FxRate> fxRates = fxRateService.getRatesForEurToSpecificCurrencies();

        assertEquals(4, fxRates.size());

        FxRate usdRate = fxRates.stream().filter(rate -> "USD".equals(rate.getTargetCurrency())).findFirst().orElse(null);
        assertEquals("EUR", usdRate.getSourceCurrency());
        assertEquals("USD", usdRate.getTargetCurrency());
        assertEquals(1.0789, usdRate.getExchangeRate());
        assertEquals("2024-03-18", usdRate.getDate());

        FxRate gbpRate = fxRates.stream().filter(rate -> "GBP".equals(rate.getTargetCurrency())).findFirst().orElse(null);
        assertEquals("EUR", gbpRate.getSourceCurrency());
        assertEquals("GBP", gbpRate.getTargetCurrency());
        assertEquals(0.84328, gbpRate.getExchangeRate());
        assertEquals("2024-03-18", gbpRate.getDate());

        FxRate jpyRate = fxRates.stream().filter(rate -> "JPY".equals(rate.getTargetCurrency())).findFirst().orElse(null);
        assertEquals("EUR", jpyRate.getSourceCurrency());
        assertEquals("JPY", jpyRate.getTargetCurrency());
        assertEquals(162.66, jpyRate.getExchangeRate());
        assertEquals("2024-03-18", jpyRate.getDate());

        FxRate czkRate = fxRates.stream().filter(rate -> "CZK".equals(rate.getTargetCurrency())).findFirst().orElse(null);
        assertEquals("EUR", czkRate.getSourceCurrency());
        assertEquals("CZK", czkRate.getTargetCurrency());
        assertEquals(25.454, czkRate.getExchangeRate());
        assertEquals("2024-03-18", czkRate.getDate());
    }
}
