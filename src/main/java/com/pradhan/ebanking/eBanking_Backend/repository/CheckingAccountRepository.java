package com.pradhan.ebanking.eBanking_Backend.repository;

import com.pradhan.ebanking.eBanking_Backend.beans.Checking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CheckingAccountRepository extends JpaRepository<Checking, Long> {
}
