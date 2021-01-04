package com.pradhan.ebanking.eBanking_Backend.dto;

import java.util.Date;

public class TransferAmountInfo {

    private String senderUserName;
    private String senderAccountType;
    private int otherAcctNumber;
    private String accountType;

    public String getSenderUserName() {
        return senderUserName;
    }

    public String getSenderAccountType() {
        return senderAccountType;
    }

    public int getOtherAcctNumber() {
        return otherAcctNumber;
    }

    public String getAccountType() {
        return accountType;
    }
}
