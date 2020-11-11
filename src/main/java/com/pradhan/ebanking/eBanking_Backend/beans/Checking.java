package com.pradhan.ebanking.eBanking_Backend.beans;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


@Entity
public class Checking {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private BigDecimal balance;
    private Date withdrawalDate;
    private Date depositDate;

    @OneToOne
    @JsonBackReference
    private Account account;


    public Checking() {
    }

    public Checking(BigDecimal balance, Date withdrawalDate, Date depositDate, Account account) {
        this.balance = balance;
        this.withdrawalDate = withdrawalDate;
        this.depositDate = depositDate;
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

    public Date getWithdrawalDate() {
        return withdrawalDate;
    }

    public void setWithdrawalDate(Date withdrawalDate) {
        this.withdrawalDate = withdrawalDate;
    }

    public Date getDepositDate() {
        return depositDate;
    }

    public void setDepositDate(Date depositDate) {
        this.depositDate = depositDate;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}
