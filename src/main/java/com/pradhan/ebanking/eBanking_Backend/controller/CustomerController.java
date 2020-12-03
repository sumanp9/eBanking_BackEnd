package com.pradhan.ebanking.eBanking_Backend.controller;

import com.pradhan.ebanking.eBanking_Backend.beans.*;
import com.pradhan.ebanking.eBanking_Backend.dto.*;
import com.pradhan.ebanking.eBanking_Backend.repository.AccountRepository;
import com.pradhan.ebanking.eBanking_Backend.repository.CheckingAccountRepository;
import com.pradhan.ebanking.eBanking_Backend.repository.CustomerRepository;
import com.pradhan.ebanking.eBanking_Backend.repository.SavingsAccountRepository;
import com.pradhan.ebanking.eBanking_Backend.service.AccountService;
import com.pradhan.ebanking.eBanking_Backend.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

import java.util.Random;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private AccountService accountService;

    @GetMapping("/")
    public String homepage(){
        return "<h1< Hello! homepage </h1>";
    }

    @PostMapping("/login")
    public Customer loginUser(@RequestBody CustomerDto customerDto) throws Exception {
        return this.customerService.loginUser(customerDto);
   }

    @PostMapping("/registerCustomer")
    public Customer registerUser(@RequestBody CustomerDto customerDto) {
        Customer customer = customerService.registerUser(customerDto);
        Account account = accountService.registerNewAccount(customer);
        accountService.registerCheckingAndSavingsAccount(account);
        // TODO: check if the accountId aleady exisits after creating one above.
        return customer;
    }

    @PostMapping("/getCustomerInfo")
    public Customer getCustomerInfo(@RequestBody String userName) throws Exception {
        Customer customer =  customerService.findByUserName(userName);
        return customer;
    }

    @PostMapping("/getAccountDetails")
    public AccountDetails getAccountDetails(@RequestBody String userName) throws Exception {
        Account account = customerService.getUserAccount(userName);
        AccountDetails accountDetails = accountService.getAccountDetails(account);
        return accountDetails;
    }

    @PostMapping("/getSavings")
    public Savings getSavingsInfo(@RequestBody String userName) throws Exception{
        if (customerService.isValidUser(userName)) {
            Account account = customerService.getUserAccount(userName);
            return accountService.getSavingsInfo(account.getAccountId());
        } else {
            throw new Exception("Username: " + userName+ " does not exists!");
        }

    }

    @PostMapping("/depositSavings/{amount}")
    public Boolean depositSavings(@PathVariable long amount, @RequestBody long savingsId) throws Exception {
        return accountService.depositSavings(amount, savingsId);
    }

    @PostMapping("/withdrawSavings/{amount}")
    public Boolean withdrawSavings(@PathVariable long amount, @RequestBody long savingsId) throws Exception {

        return accountService.withdrawSavings(amount, savingsId);

    }

    @PostMapping("/transferTo/checking/{amount}")
    public void transferToChecking(@PathVariable long amount, @RequestBody String userName) throws Exception {
            if (customerService.isValidUser(userName)) {
                Account account =  customerService.findByUserName(userName).getUserAccount();
                accountService.transferToChecking(account, amount);
            } else {
                throw new NullPointerException("Customer does not exist");
            }
    }

    @PostMapping("/transferTo/anotherAccount/{amount}")
    public void transferToOthersAccount( @PathVariable int amount, @RequestBody TransferAmountInfo transferInfo) {
        this.customerService.isValidUser(transferInfo.getSenderUserName());
        this.accountService.isValidAccountNumber(transferInfo.getOtherAccountNumber());
    }

}
