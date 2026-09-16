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
    
     // These fields store the details of the one registered user.
    private String username;
    private String password;
    private String cellPhoneNumber;
    private String firstName;
    private String lastName;
    private boolean registered = false;
 
    /**
     * Checks that the username contains an underscore and is
     * no more than five characters long.
     */
    public boolean checkUserName(String username) {
        // ^ start, must contain at least one underscore, 1 to 5 characters total, $ end
        return username != null && username.matches("^(?=.*_).{1,5}$");
    }
 
    /**
     * Checks that the password is at least 8 characters long and
     * contains a capital letter, a number and a special character.
     */
    public boolean checkPasswordComplexity(String password) {
        if (password == null) {
            return false;
        }
        // (?=.*[A-Z]) at least one capital letter
        // (?=.*[0-9]) at least one number
        // (?=.*[^a-zA-Z0-9]) at least one special character
        // .{8,} at least 8 characters long
        return password.matches("^(?=.*[A-Z])(?=.*[0-9])(?=.*[^a-zA-Z0-9]).{8,}$");
    }
 
    /**
     * Checks that the cell phone number starts with the South African
     * international code (+27) followed by up to ten more digits.
     */
    public boolean checkCellPhoneNumber(String cellPhoneNumber) {
        // ^\+27 must start with +27, then 1 to 10 digits, $ end
        return cellPhoneNumber != null && cellPhoneNumber.matches("^\\+27[0-9]{1,10}$");
    }
 
    /**
     * Registers a new user if the username and password are valid.
     * If both are valid, the user's details (including the cell phone
     * number and name) are stored and the user is marked as registered.
     */
    public String registerUser(String firstName, String lastName, String username,
                                String password, String cellPhoneNumber) {
 
        if (!checkUserName(username)) {
            return "Username is not correctly formatted; please ensure that your username "
                    + "contains an underscore and is no more than five characters in length.";
        }
 
        if (!checkPasswordComplexity(password)) {
            return "Password is not correctly formatted; please ensure that the password "
                    + "contains at least eight characters, a capital letter, a number, and "
                    + "a special character.";
        }
 
        // Username and password are both valid, so we store the user's details.
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.cellPhoneNumber = cellPhoneNumber;
        this.registered = true;
 
        return "User " + username + " successfully registered.";
    }
 
    /**
     * Checks whether the given username and password match the
     * details of the registered user.
     */
    public boolean loginUser(String username, String password) {
        if (!registered) {
            return false;
        }
        return this.username.equals(username) && this.password.equals(password);
    }
 
    /**
     * Returns the correct message depending on whether the login
     * attempt was successful or not.
     */
    public String returnLoginStatus(boolean loginSuccessful) {
        if (loginSuccessful) {
            return "Welcome " + firstName + ", " + lastName + " it is great to see you again.";
        } else {
            return "Username or password incorrect, please try again.";
        }
    }
 
    // Simple getters, used by Main.java to display captured details.
    public String getFirstName() {
        return firstName;
    }
 
    public String getLastName() {
        return lastName;
    }
 
    public String getCellPhoneNumber() {
        return cellPhoneNumber;
    }
}
