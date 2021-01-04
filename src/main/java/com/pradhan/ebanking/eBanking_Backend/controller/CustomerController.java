package com.pradhan.ebanking.eBanking_Backend.controller;

import com.pradhan.ebanking.eBanking_Backend.beans.*;
import com.pradhan.ebanking.eBanking_Backend.dto.*;
import com.pradhan.ebanking.eBanking_Backend.service.AccountService;
import com.pradhan.ebanking.eBanking_Backend.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/getUserId")
    public long getUserId(@RequestBody String userName) throws Exception {
        return customerService.getUserId(userName);
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

    @PostMapping("/getChecking")
    public Checking getCheckingInfo(@RequestBody long customerId) throws Exception {
            String userName =  customerService.getUserName(customerId);
            Account account = customerService.getUserAccount(userName);
            return accountService.getCheckingInfo(account.getAccountId());
    }

    @PostMapping("/depositSavings/{amount}")
    public Boolean depositSavings(@PathVariable long amount, @RequestBody long savingsId) throws Exception {
        return accountService.depositSavings(amount, savingsId);
    }

    @PostMapping("/withdrawSavings/{amount}")
    public Boolean withdrawSavings(@PathVariable long amount, @RequestBody long savingsId) throws Exception {

        return accountService.withdrawSavings(amount, savingsId);

    }

    @PostMapping("/transferTo/{accountTypeTo}/{amount}")
    public void transferTo(@PathVariable long amount, @PathVariable String accountTypeTo, @RequestBody String userName) throws Exception {
            if (customerService.isValidUser(userName)) {
                Account account =  customerService.findByUserName(userName).getUserAccount();
                accountService.transferToAccount(account, amount, accountTypeTo);
            } else {
                throw new NullPointerException("Customer does not exist");
            }
    }

    @PostMapping("/transferTo/anotherAccount/{amount}")
    public void transferToOthersAccount( @PathVariable long amount, @RequestBody TransferAmountInfo transferInfo) throws Exception {
            Account senderAccount =  this.customerService.getUserAccount(transferInfo.getSenderUserName());
            Account receivingAccount = this.accountService.getAccount(transferInfo.getOtherAcctNumber());
            accountService.transferToOtherAccount(senderAccount, transferInfo.getSenderAccountType(), receivingAccount, amount);

    }

}
