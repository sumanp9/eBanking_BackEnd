package com.pradhan.ebanking.eBanking_Backend.beans;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
public class TransactionHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private long account_id;
    private Date date;
    private String fromAccount;
    private String toAccount;
    private BigDecimal amount;


    @JsonIgnore
    @ManyToOne(optional = true, fetch = FetchType.LAZY) // optional = true; -> transaction can be between checking and another bank account
    @JoinColumn(name = "savings_id")
    private Savings savings;

    @JsonIgnore
    @ManyToOne(optional = true, fetch = FetchType.LAZY) // optional = true -> transaction can be between savings and another bank account
    @JoinColumn(name = "checking_id")
    private Checking checking;


    public TransactionHistory() {
    }

    public TransactionHistory(long account_id, Date date, String fromAccount, String toAccount, BigDecimal amount) {
        this.account_id = account_id;
        this.date = date;
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.amount = amount;
    }


    public long getId() {
        return id;
    }

    public long getAccount_id() {
        return account_id;
    }

    public void setAccount_id(long account_id) {
        this.account_id = account_id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getFromAccount() {
        return fromAccount;
    }

    public void setFromAccount(String fromAccount) {
        this.fromAccount = fromAccount;
    }

    public String getToAccount() {
        return toAccount;
    }

    public void setToAccount(String toAccount) {
        this.toAccount = toAccount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Savings getSavings() {
        return savings;
    }

    public void setSavings(Savings savings) {
        this.savings = savings;
    }

    public Checking getChecking() {
        return checking;
    }

    public void setChecking(Checking checking) {
        this.checking = checking;
    }
}
