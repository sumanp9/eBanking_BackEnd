package dto;

import com.pradhan.ebanking.eBanking_Backend.beans.Account;

public class CustomerDto {

    private long id;

    private String firstName;
    private String lastName;
    private String userName;
    private String email;
    private String password;

    private Account account;


    public long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }

    public Account getAccount() {
        return account;
    }

    public String getPassword() {
        return password;
    }
}
