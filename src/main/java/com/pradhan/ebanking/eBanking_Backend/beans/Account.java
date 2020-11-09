package com.pradhan.ebanking.eBanking_Backend.beans;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;


@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private int accountId;

    @OneToOne()
    @JsonBackReference
    private Customer customer;

    public Account() {
    }

    public Account(int accountId) {
        this.accountId = accountId;
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

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", accountId=" + accountId +
                ", customer=" + customer +
                '}';
    }
}

