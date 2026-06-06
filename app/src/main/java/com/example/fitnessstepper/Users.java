package com.example.fitnessstepper;

public class Users {

    private String emailAddress;
    private String Password;

    public Users(String emailAddress, String password) {
        this.emailAddress = emailAddress;
        Password = password;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

}
