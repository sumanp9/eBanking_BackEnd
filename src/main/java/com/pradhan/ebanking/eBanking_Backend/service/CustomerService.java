package com.pradhan.ebanking.eBanking_Backend.service;

import com.pradhan.ebanking.eBanking_Backend.beans.Account;
import com.pradhan.ebanking.eBanking_Backend.beans.Customer;
import com.pradhan.ebanking.eBanking_Backend.dto.CustomerDto;
import com.pradhan.ebanking.eBanking_Backend.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;


    public Customer loginUser(CustomerDto customerDto){
        if(!customerDto.getUserName().isEmpty() && !customerDto.getPassword().isEmpty()) {
            return this.customerRepository.findByUserName(customerDto.getUserName());
        } else {
            throw new IllegalArgumentException("Username and password invalid");
        }
    }

    public List<Customer> getAllCustomer() {
        return this.customerRepository.findAll();
    }

    public Customer registerUser(CustomerDto customerDto) {
        //TODO: Check if username or email already exists
        if (isRegisteredUser(customerDto)) {
            //if(customerRepository.findByEmail(customerDto.getEmail() ))
            Customer customer = createCustomer(customerDto);
            return customerRegister(customer);
            //customerRepository.save(customer);
            //return customer;
        } else {
            throw new IllegalArgumentException(customerDto.getUserName()+ " already a registered user.");
        }

    }

    public Customer customerRegister(Customer customer) {
        customerRepository.save(customer);
        return customer;
    }

    private boolean isRegisteredUser(CustomerDto customerDto) {
        if (!isValidUser(customerDto.getUserName()) || !validEmail(customerDto.getEmail())) {
            return true;
        } else  return  false;
    }

    private boolean validEmail(String email) {
        List<String> emailList =  new ArrayList<>();
        this.customerRepository.findAll().stream().map(customer -> emailList.add(customer.getEmail()));
        if (emailList.contains(email)) {
            return true;
        } else return false;
    }

    private Customer createCustomer(CustomerDto customerDto) {
        return new Customer(
                customerDto.getFirstName(), customerDto.getLastName(),
                customerDto.getUserName(), customerDto.getPassword(),
                customerDto.getEmail(), customerDto.getAddress(),
                customerDto.getPhoneNumber()
        );
    }


    public Account getUserAccount(String userName) throws Exception{
        if (isValidUser(userName)) {
            return customerRepository.findByUserName(userName).getUserAccount();
        } else {
            throw new Exception("Username: " + userName+ " does not exists!");
        }
    }

    public boolean isValidUser(String userName) {
        List<Customer> customerList =  this.customerRepository.findAll();
        List<String> users = new ArrayList();
        customerList.forEach(customer -> {
            users.add(customer.getUserName());
        });
        if(!userName.isEmpty() && users.contains(userName)) {
            return true;
        } else
        {
            return false;
        }
    }

    public Customer findByUserName(String userName) throws Exception {
        if (isValidUser(userName)) {
            System.out.println("Finding here "+ userName);
            return  customerRepository.findByUserName(userName);
        } else
            throw new Exception("Username: "+ userName+ " does not exists");
    }
}
