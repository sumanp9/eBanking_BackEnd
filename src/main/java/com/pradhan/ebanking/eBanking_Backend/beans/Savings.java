package com.pradhan.ebanking.eBanking_Backend.beans;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
public class Savings {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private BigDecimal balance;

    @OneToOne(cascade = CascadeType.ALL)
    @JsonBackReference
    private Account account;

    @OneToMany(mappedBy = "savings", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<TransactionHistory> transactionHistory;

    public Savings() {
    }

    public Savings(BigDecimal balance, Account account) {
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

    public List<TransactionHistory> getTransactionHistory() {
        return transactionHistory;
    }

    public void setTransactionHistory(List<TransactionHistory> transactionHistory) {
        this.transactionHistory = transactionHistory;
    }
}
