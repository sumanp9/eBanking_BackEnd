package com.pradhan.ebanking.eBanking_Backend.beans;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.math.BigDecimal;


@Entity
public class Checking {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private BigDecimal balance;


    @OneToOne
    @JsonBackReference
    private Account account;


    public Checking() {
    }

    public Checking(BigDecimal balance, Account account) {
        this.balance = balance;
        this.account = account;
    }

    public long getId() {
        return id;
    }


    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}
