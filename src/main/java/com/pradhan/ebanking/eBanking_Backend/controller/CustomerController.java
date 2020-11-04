package com.pradhan.ebanking.eBanking_Backend.controller;


import com.pradhan.ebanking.eBanking_Backend.beans.Account;
import com.pradhan.ebanking.eBanking_Backend.beans.Customer;
import com.pradhan.ebanking.eBanking_Backend.dto.CustomerDto;
import org.springframework.web.bind.annotation.*;

import java.util.Random;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class CustomerController {

    @GetMapping("/")
    public String homepage(){
        return "<h1< Hello! homepage </h1>";
    }

    @GetMapping("/getCustomer/{username}")
    public Customer loginUser(@PathVariable(value = "id") String username, @RequestBody String password) throws Exception {
        if (!username.isEmpty() && !password.isEmpty()) {
            //Return the username
        }
         else {
             throw new Exception("Username not provided");
        }
        return null;
    }

    @PostMapping("/registerCustomer")
    public Customer registerUser(@RequestBody CustomerDto customerDto) {

        //TODO: Check if username or email already exists
        Customer cust = new Customer(
                customerDto.getFirstName(), customerDto.getLastName(),
                customerDto.getUserName(), customerDto.getPassword(),
                customerDto.getEmail()
        );
        Account acct =  new Account();

        // Using RNG to generate account number between 111111111 to 999999999
        Random rand =  new Random();
        int low = 111111;
        int limit = 999999;
        int rand_acct_num =  rand.nextInt(limit-low) + low;
        acct.setAccountId(rand_acct_num);
        acct.setCustomer(cust);
        cust.setUserAccount(acct);
        // TODO: check if the accountId aleady exisits after creating one above.
        return  cust;
    }
}
