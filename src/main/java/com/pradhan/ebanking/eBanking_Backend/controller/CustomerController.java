package com.pradhan.ebanking.eBanking_Backend.controller;

import com.pradhan.ebanking.eBanking_Backend.beans.*;
import com.pradhan.ebanking.eBanking_Backend.dto.*;
import com.pradhan.ebanking.eBanking_Backend.repository.AccountRepository;
import com.pradhan.ebanking.eBanking_Backend.repository.CheckingAccountRepository;
import com.pradhan.ebanking.eBanking_Backend.repository.CustomerRepository;
import com.pradhan.ebanking.eBanking_Backend.repository.SavingsAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

import java.util.Random;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class CustomerController {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private SavingsAccountRepository savingsRepository;

    @Autowired
    private CheckingAccountRepository checkingRepository;

    @GetMapping("/")
    public String homepage(){
        return "<h1< Hello! homepage </h1>";
    }

    @PostMapping("/login")
    public Customer loginUser(@RequestBody CustomerDto customerDto) throws Exception {
        if (!customerDto.getUserName().isEmpty() && !customerDto.getPassword().isEmpty()) {
            //Return the username
            return  this.customerRepository.findByUserName(customerDto.getUserName());
        }
         else {
             throw new Exception("Username not provided");
        }
   }

    @PostMapping("/registerCustomer")
    public Customer registerUser(@RequestBody CustomerDto customerDto) {

        //TODO: Check if username or email already exists
        Customer cust = new Customer(
                customerDto.getFirstName(), customerDto.getLastName(),
                customerDto.getUserName(), customerDto.getPassword(),
                customerDto.getEmail(), customerDto.getAddress(),
                customerDto.getPhoneNumber()
        );
        customerRepository.save(cust);

        int low = 111111;
        int limit = 999999;
        Random rand =  new Random();
        int rand_acct_num =  rand.nextInt(limit-low) + low;
        Account acct =  new Account(rand_acct_num, cust);
        System.out.println(acct.toString());
        System.out.println(cust.toString());
        accountRepository.save(acct);

        createCheckingAndSavingsAccount(acct);

        // TODO: check if the accountId aleady exisits after creating one above.
        return  cust;
    }

    @PostMapping("/getAccountDetails")
    public AccountDetails getAccountDetails(@RequestBody String userName) {
        // if () checks if account is valid and exists

        Account account =  customerRepository.findByUserName(userName).getUserAccount();

        Savings custSavings  =  savingsRepository.findByAccountId(account.getId());
        Checking  custChecking =  checkingRepository.findByAccountId(account.getId());

        AccountDetails accountDetails = new AccountDetails();
        accountDetails.setCheckingId(custChecking.getId());
        accountDetails.setCheckingBalance(custChecking.getBalance());
        accountDetails.setSavingsId(custSavings.getId());
        accountDetails.setSavingsBalance(custSavings.getBalance());

        return accountDetails;
    }

    @PostMapping("/getSavings")
    public Savings getSavingsInfo(@RequestBody String userName) throws Exception{
        if (!userName.isEmpty() && userName != null) {
            Account account =  customerRepository.findByUserName(userName).getUserAccount();
            Savings savings = accountRepository.findAccountByAccountId(account.getAccountId()).getSavings();
            return savings;
        } else {
            throw new Exception("The account number is empty or null!");
        }
    }

    @PostMapping("/depositSavings/{amount}")
    public Boolean depositSavings(@PathVariable long amount, @RequestBody long savingsId) throws Exception {

        if (savingsId > 0) {
            Savings savings = savingsRepository.findById(savingsId).get();
            BigDecimal oldAmount = savings.getBalance();
            BigDecimal depositAmount = new BigDecimal(amount);
            BigDecimal newAmount = oldAmount.add(depositAmount);
            savings.setBalance(newAmount);
            savingsRepository.save(savings);
            return true;
        } else {
            throw new Exception("Users cannot have savingsId 0 or less");
        }
    }

    @PostMapping("/withdrawSavings/{amount}")
    public Boolean withdrawSavings(@PathVariable long amount, @RequestBody long savingsId) throws Exception {
        if (savingsId > 0) {
            Savings savings =  savingsRepository.findById(savingsId).get();
            if (amount <= savings.getBalance().longValue()) {
                BigDecimal withdrawAmount =  new BigDecimal(amount);
                BigDecimal newAmount =  savings.getBalance().subtract(withdrawAmount);
                savings.setBalance(newAmount);
                savingsRepository.save(savings);
                return true;
            } else {
                throw new ArithmeticException("Withdraw amount is greater than remaining balance.");
            }
        } else {
            throw new Exception("Users cannot have savingsId 0");
        }
    }

    @PostMapping("/transferTo/checking/{amount}")
    public void transferToChecking(@PathVariable long amount, @RequestBody String userName) throws Exception {
        if (!userName.isEmpty() || userName != null) {
            Customer customer = customerRepository.findByUserName(userName);
            if (customer != null) {
                Savings savings = customer.getUserAccount().getSavings();
                Checking checking = customer.getUserAccount().getChecking();
                savings.setBalance(savings.getBalance().subtract(new BigDecimal(amount)));
                // TODO: Why is it everytime null
                if (checking.getBalance() == null) {
                    checking.setBalance(new BigDecimal(amount));
                } else {
                    checking.setBalance(checking.getBalance().add(new BigDecimal(amount)));
                }
                //System.out.println(checking.getBalance());
                savingsRepository.save(savings);
                checkingRepository.save(checking);
            } else {
                throw new NullPointerException("Customer is null");
            }

        } else{
            throw new Exception("Username is not valid" + userName);
        }
    }


    private void createCheckingAndSavingsAccount(Account acct) {
        //Assuming new users have not deposited yet.
        Savings savings = new Savings(new BigDecimal(0),acct);
        Checking checking =  new Checking(new BigDecimal(0), acct);
        savingsRepository.save(savings);
        checkingRepository.save(checking);

    }
}
