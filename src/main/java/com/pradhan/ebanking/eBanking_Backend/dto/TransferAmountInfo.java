package com.pradhan.ebanking.eBanking_Backend.dto;

import java.util.Date;

public class TransferAmountInfo {

    private String senderUserName;
    private int otherAcctNumber;
    private String accountType;
    private Date date;


    public String getSenderUserName() {
        return senderUserName;
    }

    public int getOtherAcctNumber() {
        return otherAcctNumber;
    }

    public String getAccountType() {
        return accountType;
    }
}
