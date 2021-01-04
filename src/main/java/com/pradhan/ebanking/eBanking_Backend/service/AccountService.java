package com.pradhan.ebanking.eBanking_Backend.service;

import com.pradhan.ebanking.eBanking_Backend.beans.*;
import com.pradhan.ebanking.eBanking_Backend.enums.AccountType;
import com.pradhan.ebanking.eBanking_Backend.repository.AccountRepository;
import com.pradhan.ebanking.eBanking_Backend.repository.CheckingAccountRepository;
import com.pradhan.ebanking.eBanking_Backend.repository.SavingsAccountRepository;
import com.pradhan.ebanking.eBanking_Backend.repository.TransferHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
public class AccountService {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    SavingsAccountRepository savingsRepository;

    @Autowired
    CheckingAccountRepository checkingRepository;

    @Autowired
    TransferHistoryRepository transferHistoryRepository;


    public Account registerNewAccount(Customer customer) {

        int low = 111111;
        int limit = 999999;
        Random rand =  new Random();
        int rand_acct_num =  rand.nextInt(limit-low) + low;
        Account account =  new Account(rand_acct_num, customer);
        accountRepository.save(account);
        return account;
    }

    public Account getAccount(long accountId) throws Exception {
        if (isValidAccountNumber(accountId)) {
            return this.accountRepository.findAccountByAccountId(accountId);
        } else {
            throw new Exception("Invalid Account");
        }
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
        return accountRepository.findAccountByAccountId(accountId).getSavings();
    }
    public Checking getCheckingInfo(long accountId) {
        return checkingRepository.findCheckingByAccount_AccountId(accountId);
    }

    public boolean depositSavings(long amount, long savingsId) throws NullPointerException {

        if (isValidSavingsId(savingsId)) {
            Savings savings = savingsRepository.findById(savingsId).get();
            BigDecimal oldAmount = savings.getBalance();
            BigDecimal depositAmount = new BigDecimal(amount);
            BigDecimal newAmount = oldAmount.add(depositAmount);
            savings.setBalance(newAmount);
            savingsRepository.save(savings);

            // TODO: need transactionType in Transaction History
            recordSavingsDeposit(amount, savings.getAccount().getId(), savings);
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


    public void transferToAccount(Account account, long amount, String accountTypeTo) {
        Savings savings =  savingsRepository.findSavingsByAccount_AccountId(account.getAccountId());
        Checking checking = checkingRepository.findCheckingByAccount_AccountId(account.getAccountId());
        if (accountTypeTo.equals("checking")) {
            savings.setBalance(savings.getBalance().subtract(new BigDecimal(amount)));
            if (checking.getBalance().longValue() <= 0) {
                checking.setBalance(new BigDecimal(amount));
            } else {
                checking.setBalance(checking.getBalance().add(new BigDecimal(amount)));
            }
            recordSavingsToCheckingAccount(amount, savings.getAccount().getId(), savings, checking);
        } else{
            checking.setBalance(checking.getBalance().subtract(new BigDecimal(amount)));
            if (savings.getBalance().longValue() <=0) {
                savings.setBalance(new BigDecimal(amount));
            } else {
                savings.setBalance(savings.getBalance().add(new BigDecimal(amount)));
            }
            recordCheckingToSavingsAccount(amount, checking.getAccount().getId(), checking, savings);
        }
        savingsRepository.save(savings);
        checkingRepository.save(checking);

    }

    public boolean isValidAccountNumber(long accountNumber) {
        System.out.println(accountNumber);
        if (Long.valueOf(accountNumber) !=null) {
            if (this.accountRepository.findAccountByAccountId(accountNumber)!=null) {
                return true;
            }
            else {
                throw new NullPointerException("Account cannot be retrieved");
            }
        } else {
            throw new NullPointerException("Account Number provided is null");
        }

    }

    public void transferToOtherAccount(Account senderAccount, String senderAccountType, Account receivingAccount, long amount) {
        Savings receivingSavings = receivingAccount.getSavings();
        BigDecimal amt = new BigDecimal(amount);
        if (senderAccountType.equals("Savings")) {
            Savings senderSavings = (senderAccount.getSavings());
            BigDecimal savingsBal = senderSavings.getBalance();
            if (savingsBal.compareTo(amt) == 0 || savingsBal.compareTo(amt) == 1) {
                senderSavings.setBalance(savingsBal.subtract(amt));
                receivingSavings.setBalance(receivingSavings.getBalance().add(amt));
                savingsRepository.save(senderSavings);
                savingsRepository.save(receivingSavings);
                recordToSeparateAccount(amt, senderAccount, receivingAccount.getAccountId(), senderAccountType);

            } else {
                throw new ArithmeticException("Savings Balance is lower than transferring amount");
            }
        } else if (senderAccountType.equals("Checking")) {
            Checking senderChecking = senderAccount.getChecking();
            BigDecimal checkingBal = senderChecking.getBalance();
            System.out.println("Here in checking with checking balance -> "+ checkingBal.subtract(amt));
            System.out.println(checkingBal.compareTo(amt) == 1 || checkingBal.compareTo(amt) == 0);

            if (checkingBal.compareTo(amt) == 0 || checkingBal.compareTo(amt) == 1) {
                senderChecking.setBalance(checkingBal.subtract(amt));
                receivingSavings.setBalance(receivingSavings.getBalance().add(amt));
                checkingRepository.save(senderChecking);
                savingsRepository.save(receivingSavings);
                recordToSeparateAccount(amt, senderAccount, receivingAccount.getAccountId(), senderAccountType);

            } else {
                throw new ArithmeticException("Checking Balance is lower than transferring amount");
            }
        }
    }

    private void recordSavingsDeposit(long amount, long accountId, Savings savings) {
        TransactionHistory transactionHistory =  new TransactionHistory();
        Date date = new Date();
        transactionHistory.setFromAccount(AccountType.SAVINGS.toString());
        transactionHistory.setToAccount(AccountType.SAVINGS.toString());
        transactionHistory.setAmount(new BigDecimal(amount));
        transactionHistory.setDate(date);
        transactionHistory.setSavings(savings);
        transactionHistory.setAccount_id(accountId);
        transferHistoryRepository.save(transactionHistory);

    }

    private void recordCheckingDeposit(long amount, long accountId, Checking checking) {
        TransactionHistory transactionHistory =  new TransactionHistory();
        Date date = new Date();
        transactionHistory.setFromAccount(AccountType.CHECKING.toString());
        transactionHistory.setToAccount(AccountType.CHECKING.toString());
        transactionHistory.setAmount(new BigDecimal(amount));
        transactionHistory.setDate(date);
        transactionHistory.setChecking(checking);
        transactionHistory.setAccount_id(accountId);
        transferHistoryRepository.save(transactionHistory);
    }

    private void recordSavingsToCheckingAccount( long amount, long accountId, Savings savings, Checking checking) {
        TransactionHistory transactionHistory =  new TransactionHistory();
        Date date = new Date();
        transactionHistory.setFromAccount(AccountType.SAVINGS.toString());
        transactionHistory.setToAccount(AccountType.CHECKING.toString());
        transactionHistory.setAmount(new BigDecimal(amount));
        transactionHistory.setDate(date);
        transactionHistory.setSavings(savings);
        transactionHistory.setChecking(checking);
        transactionHistory.setAccount_id(accountId);
        transferHistoryRepository.save(transactionHistory);
    }


    private void recordCheckingToSavingsAccount(long amount, long accountId, Checking checking, Savings savings) {
        TransactionHistory transactionHistory =  new TransactionHistory();
        Date date = new Date();
        transactionHistory.setFromAccount(AccountType.CHECKING.toString());
        transactionHistory.setToAccount(AccountType.SAVINGS.toString());
        transactionHistory.setAmount(new BigDecimal(amount));
        transactionHistory.setDate(date);
        transactionHistory.setSavings(savings);
        transactionHistory.setChecking(checking);
        transactionHistory.setAccount_id(accountId);
        transferHistoryRepository.save(transactionHistory);
    }

    private void recordToSeparateAccount(BigDecimal amount, Account account, long receivingAccountId, String senderAccountType) {
        TransactionHistory transactionHistory = new TransactionHistory();
        Date date =  new Date();
        if (senderAccountType.equals("Savings")) {
            transactionHistory.setFromAccount(AccountType.SAVINGS.toString());
            transactionHistory.setToAccount(lastFourNum(receivingAccountId));
            transactionHistory.setAmount(amount);
            transactionHistory.setDate(date);
            transactionHistory.setSavings(account.getSavings()); //TODO: test this, check if it produces error or not
            transactionHistory.setChecking(null);
            transactionHistory.setAccount_id(account.getId());
            transferHistoryRepository.save(transactionHistory); // TODO: transaction history repository >>> transferHistoryRepo;
        } else if (senderAccountType.equals("Checking")) {
            transactionHistory.setFromAccount(AccountType.CHECKING.toString());
            transactionHistory.setToAccount(lastFourNum(receivingAccountId));
            transactionHistory.setAmount(amount);
            transactionHistory.setDate(date);
            transactionHistory.setChecking(account.getChecking()); //TODO: test this, check if it produces error or not
            transactionHistory.setSavings(null);
            transactionHistory.setAccount_id(account.getId());
            transferHistoryRepository.save(transactionHistory); // TODO: transaction history repository >>> transferHistoryRepo;
        }
    }

    private String lastFourNum(long receivingAccountId) {
        String lastFour = "";
        String accountId = String.valueOf(receivingAccountId);
        int idLength = accountId.length();
        lastFour = accountId.substring(idLength-4, idLength);
        for (int i =0; i <= (idLength - lastFour.length()); i++) {
            lastFour = "*"+lastFour;
        }
        return lastFour;
    }


}
