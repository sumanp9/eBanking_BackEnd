package com.pradhan.ebanking.eBanking_Backend.beans;


import javax.persistence.*;

@Entity
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String firstName;
    private String lastName;
    private String userName;
    private String email;
    private String password;

    @OneToOne(mappedBy = "customer")
    private Account userAccount;

    public Customer() {
    }

    public Customer(String firstName, String lastName, String userName, String password, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.email = email;
        this.password = password;
    }

    public long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Account getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(Account userAccount) {
        this.userAccount = userAccount;
    }
}
