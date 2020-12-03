package com.pradhan.ebanking.eBanking_Backend.service;

import com.pradhan.ebanking.eBanking_Backend.beans.*;
import com.pradhan.ebanking.eBanking_Backend.repository.AccountRepository;
import com.pradhan.ebanking.eBanking_Backend.repository.CheckingAccountRepository;
import com.pradhan.ebanking.eBanking_Backend.repository.SavingsAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

@Service
public class AccountService {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    SavingsAccountRepository savingsRepository;

    @Autowired
    CheckingAccountRepository checkingRepository;



    public Account registerNewAccount(Customer customer) {

        int low = 111111;
        int limit = 999999;
        Random rand =  new Random();
        int rand_acct_num =  rand.nextInt(limit-low) + low;
        Account account =  new Account(rand_acct_num, customer);
        accountRepository.save(account);
        return account;
    }

    public void registerCheckingAndSavingsAccount(Account account) {
        // 10 is the starting amount
        Savings savings = new Savings(new BigDecimal(10),account);
        Checking checking =  new Checking(new BigDecimal(10), account);
        savingsRepository.save(savings);
        checkingRepository.save(checking);
    }


    public AccountDetails getAccountDetails(Account account) {

        Checking custChecking =  this.checkingRepository.findCheckingByAccount_AccountId(account.getAccountId());
        Savings custSavings = this.savingsRepository.findSavingsByAccount_AccountId(account.getAccountId());

        AccountDetails accountDetails = new AccountDetails();
        accountDetails.setCheckingId(custChecking.getId());
        accountDetails.setCheckingBalance(custChecking.getBalance());
        accountDetails.setSavingsId(custSavings.getId());
        accountDetails.setSavingsBalance(custSavings.getBalance());

        return accountDetails;
    }

    public Savings getSavingsInfo(long accountId) {
        //TODO: Change type of Account.accountId to long
        return accountRepository.findAccountByAccountId(accountId).getSavings();
    }


    public boolean depositSavings(long amount, long savingsId) throws Exception {

        if (isValidSavingsId(savingsId)) {
            Savings savings = savingsRepository.findById(savingsId).get();
            BigDecimal oldAmount = savings.getBalance();
            BigDecimal depositAmount = new BigDecimal(amount);
            BigDecimal newAmount = oldAmount.add(depositAmount);
            savings.setBalance(newAmount);
            savingsRepository.save(savings);
            return true;

        }
         else {
            throw new NullPointerException("Savings Id cannot be null or less or equal to 0");
        }

    }

    public boolean withdrawSavings(long amount, long savingsId) throws Exception {
        if (isValidSavingsId(savingsId)) {
            if (isValidSavingsId(savingsId)) {
                Savings savings = savingsRepository.findById(savingsId).get();
                if (amount <= savings.getBalance().longValue()) {
                    BigDecimal withdrawAmount = new BigDecimal(amount);
                    BigDecimal newAmount = savings.getBalance().subtract(withdrawAmount);
                    savings.setBalance(newAmount);
                    savingsRepository.save(savings);
                    return true;
                } else {
                    throw new ArithmeticException("Withdraw amount is greater than remaining balance.");
                }
            }
            else
                throw new Exception("Unable to find Savings invalid Id");
        }
        else {
            throw new NullPointerException("Savings Id cannot be null or less or equal to 0");
        }
    }


    public boolean isValidSavingsId( long savingsId) {

        List<Account> accountList =  accountRepository.findAll();
        List<Long> savingsIdList = new ArrayList<>();
        accountList.forEach((account -> {
            savingsIdList.add(account.getSavings().getId());
        }));
        if (savingsIdList.contains(savingsId)) {
            return true;
        }
        else return false;
    }


    public void transferToChecking(Account account, long amount) {
        Savings savings =  savingsRepository.findSavingsByAccount_AccountId(account.getAccountId());
        Checking checking = checkingRepository.findCheckingByAccount_AccountId(account.getAccountId());
        savings.setBalance(savings.getBalance().subtract(new BigDecimal(amount)));
        if (checking.getBalance().longValue() <=0) {
            checking.setBalance(new BigDecimal(amount));
        } else {
            checking.setBalance(checking.getBalance().add(new BigDecimal(amount)));
        }
        savingsRepository.save(savings);
        checkingRepository.save(checking);

        //TODO: need to have transaction history containing from which account to which, date/time and amount
    }

    public boolean isValidAccountNumber(int accountNumber) {
        if (Integer.valueOf(accountNumber) !=null) {
            if (this.accountRepository.findAccountByAccountId(accountNumber)!=null) {
                return true;
            }
            else {
                throw new NullPointerException("Account cannot be retreieved");
            }
        } else {
            throw new NullPointerException("Account Number provided is null");
        }

    }
}
