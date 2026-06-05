package com.example.fitnessstepper;

public class Users {

    private String emailAddress;
    private String Password;
    private int Steps;

    public Users(String emailAddress, String password) {
        this.emailAddress = emailAddress;
        Password = password;
        Steps = 0;
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

    public int getSteps() {
        return Steps;
    }

    public void setSteps(int steps) {
        Steps = steps;
    }
}
