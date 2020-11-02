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

    private BigDecimal checkingBalance;

    private BigDecimal savingBalance;

    private Date depositDate;

    private Date withdrawalDate;

    @OneToOne
    private Customer customer;

    public Account() {
    }

    public Account(int accountId, BigDecimal checkingBalance, BigDecimal savingBalance, Date depositDate, Date withdrawalDate) {
        this.accountId = accountId;
        this.checkingBalance = checkingBalance;
        this.savingBalance = savingBalance;
        this.depositDate = depositDate;
        this.withdrawalDate = withdrawalDate;
    }

}
