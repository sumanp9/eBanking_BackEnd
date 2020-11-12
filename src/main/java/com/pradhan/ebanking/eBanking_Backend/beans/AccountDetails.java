package com.pradhan.ebanking.eBanking_Backend.beans;

import java.math.BigDecimal;

public class AccountDetails {

    private long savingsId;
    private long checkingId;

    private BigDecimal savingsBalance;
    private BigDecimal checkingBalance;

    public AccountDetails() {
    }

    public AccountDetails(long savingsId, long checkingId, BigDecimal savingsBalance, BigDecimal checkingBalance) {
        this.savingsId = savingsId;
        this.checkingId = checkingId;
        this.savingsBalance = savingsBalance;
        this.checkingBalance = checkingBalance;
    }

    public long getSavingsId() {
        return savingsId;
    }

    public long getCheckingId() {
        return checkingId;
    }

    public BigDecimal getSavingsBalance() {
        return savingsBalance;
    }

    public BigDecimal getCheckingBalance() {
        return checkingBalance;
    }

    public void setSavingsId(long savingsId) {
        this.savingsId = savingsId;
    }

    public void setCheckingId(long checkingId) {
        this.checkingId = checkingId;
    }

    public void setSavingsBalance(BigDecimal savingsBalance) {
        this.savingsBalance = savingsBalance;
    }

    public void setCheckingBalance(BigDecimal checkingBalance) {
        this.checkingBalance = checkingBalance;
    }
}
