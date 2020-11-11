package com.pradhan.ebanking.eBanking_Backend.dto;

import java.math.BigDecimal;
import java.util.Date;

public class CheckingDto {

    private long id;
    private BigDecimal balance;
    private Date withdrawalDate;
    private Date depositDate;

    public long getId() {
        return id;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public Date getWithdrawalDate() {
        return withdrawalDate;
    }

    public Date getDepositDate() {
        return depositDate;
    }
}
