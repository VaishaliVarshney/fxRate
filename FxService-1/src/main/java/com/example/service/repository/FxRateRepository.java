package com.example.service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.service.entity.FxRate;

public interface FxRateRepository extends JpaRepository<FxRate, Long> {
	 FxRate findByTargetCurrency(String targetCurrency);
}
