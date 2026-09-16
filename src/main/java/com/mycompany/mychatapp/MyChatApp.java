/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.mychatapp;

import java.util.Scanner;

/**
 *
 * @author Student
 */
public class MyChatApp {

    public static void main(String[] args) {
 
        Scanner scanner = new Scanner(System.in);
        Login login = new Login();
 
        System.out.println("=== Registration ===");
 
        System.out.print("Enter your first name: ");
        String firstName = scanner.nextLine();
 
        System.out.print("Enter your last name: ");
        String lastName = scanner.nextLine();
 
        System.out.print("Enter a username (must contain an underscore, max 5 characters): ");
        String username = scanner.nextLine();
 
        if (login.checkUserName(username)) {
            System.out.println("Username successfully captured.");
        } else {
            System.out.println("Username is not correctly formatted; please ensure that your "
                    + "username contains an underscore and is no more than five characters in length.");
        }
 
        System.out.print("Enter a password (min 8 characters, a capital letter, a number, "
                + "a special character): ");
        String password = scanner.nextLine();
 
        if (login.checkPasswordComplexity(password)) {
            System.out.println("Password successfully captured.");
        } else {
            System.out.println("Password is not correctly formatted; please ensure that the "
                    + "password contains at least eight characters, a capital letter, a number, "
                    + "and a special character.");
        }
 
        System.out.print("Enter your South African cell number (e.g. +27838968976): ");
        String cellPhoneNumber = scanner.nextLine();
 
        if (login.checkCellPhoneNumber(cellPhoneNumber)) {
            System.out.println("Cell phone number successfully added.");
        } else {
            System.out.println("Cell phone number incorrectly formatted or does not contain "
                    + "international code; correct the number and try again.");
        }
 
        // Attempt to actually register the user using all the entered details.
        String registrationResult = login.registerUser(firstName, lastName, username, password, cellPhoneNumber);
        System.out.println(registrationResult);
 
        System.out.println();
        System.out.println("=== Login ===");
 
        System.out.print("Enter your username: ");
        String loginUsername = scanner.nextLine();
 
        System.out.print("Enter your password: ");
        String loginPassword = scanner.nextLine();
 
        boolean loginSuccessful = login.loginUser(loginUsername, loginPassword);
        String loginMessage = login.returnLoginStatus(loginSuccessful);
        System.out.println(loginMessage);
 
        scanner.close();
    
    }
}
