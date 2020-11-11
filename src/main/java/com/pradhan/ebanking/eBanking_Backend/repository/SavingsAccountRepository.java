package com.pradhan.ebanking.eBanking_Backend.repository;

import com.pradhan.ebanking.eBanking_Backend.beans.Savings;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SavingsAccountRepository extends JpaRepository<Savings, Long> {
}
