package com.pradhan.ebanking.eBanking_Backend.repository;

import com.pradhan.ebanking.eBanking_Backend.beans.TransactionHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransferHistoryRepository extends JpaRepository<TransactionHistory, Long> {

}
