/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mychatapp;

/**
 *
 * @author Student
 */
public class Login {
    
     private String username;
    private String password;
    private String cellPhoneNumber;
    private String firstName;
    private String lastName;
    private boolean registered = false;

    public boolean checkUserName(String username) {
        if (username == null || username.length() > 5) {
            return false;
        }
        return username.contains("_");
    }

    public boolean checkPasswordComplexity(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }

        boolean hasCapital = false;
        boolean hasNumber = false;
        boolean hasSpecial = false;

        for (int i = 0; i < password.length(); i++) {
            char c = password.charAt(i);

            if (Character.isUpperCase(c)) {
                hasCapital = true;
            } else if (Character.isDigit(c)) {
                hasNumber = true;
            } else if (!Character.isLetter(c)) {
                hasSpecial = true;
            }
        }

        return hasCapital && hasNumber && hasSpecial;
    }

    public boolean checkCellPhoneNumber(String cellPhoneNumber) {
        return cellPhoneNumber != null && cellPhoneNumber.matches("^\\+27[0-9]{1,10}$");
    }

    public String registerUser(String firstName, String lastName, String username,
                                String password, String cellPhoneNumber) {

        if (!checkUserName(username)) {
            return "Username is not correctly formatted please ensure that your username "
                    + "contains an underscore and is no more than five characters in length";
        }

        if (!checkPasswordComplexity(password)) {
            return "Password is not correctly formatted please ensure that the password "
                    + "contains at least eight characters, a capital letter, a number and "
                    + "a special character";
        }

        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.cellPhoneNumber = cellPhoneNumber;
        this.registered = true;

        return "User " + username + " successfully registered";
    }

    public boolean loginUser(String username, String password) {
        if (!registered) {
            return false;
        }
        return this.username.equals(username) && this.password.equals(password);
    }

    public String returnLoginStatus(boolean loginSuccessful) {
        if (loginSuccessful) {
            return "Welcome " + firstName + ", " + lastName + " it is great to see you ";
        } else {
            return "Username or password incorrect please try again";
        }
    }
}
