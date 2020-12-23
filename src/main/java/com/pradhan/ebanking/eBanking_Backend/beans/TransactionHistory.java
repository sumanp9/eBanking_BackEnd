package com.pradhan.ebanking.eBanking_Backend.beans;

import com.fasterxml.jackson.annotation.JsonBackReference;

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

    @ManyToOne
    @JsonBackReference
    private Savings savings;

    @ManyToOne
    @JsonBackReference
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
}
