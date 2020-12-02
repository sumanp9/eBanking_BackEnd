package com.pradhan.ebanking.eBanking_Backend.repository;


import com.pradhan.ebanking.eBanking_Backend.beans.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Customer findByUserName(String username);


    @Query("select email from Customer")
    List<String> findAllEmail();

    @Query("select phoneNumber from Customer")
    List<String> findAllPhoneNumber();

}
