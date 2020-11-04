package com.pradhan.ebanking.eBanking_Backend.beans;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private int accountId;

    @OneToOne
    private Customer customer;

    public Account() {
    }

    public Account(int accountId, Customer customer) {
        this.accountId = accountId;
        this.customer = customer;
    }

    public long getId() {
        return id;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}

