package com.pradhan.ebanking.eBanking_Backend.repository;

import com.pradhan.ebanking.eBanking_Backend.beans.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    Account findAccountByAccountId(long accountId);
}
