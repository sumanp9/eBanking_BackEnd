package com.pradhan.ebanking.eBanking_Backend.dto;

import com.pradhan.ebanking.eBanking_Backend.beans.Customer;

public class AccountDto {
    private long id;

    private int accountId;

    private Customer customer;

    public long getId() {
        return id;
    }

    public int getAccountId() {
        return accountId;
    }

    public Customer getCustomer() {
        return customer;
    }

}
