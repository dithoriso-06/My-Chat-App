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

        // ---------- Registration (Part 1) ----------
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

        String registrationResult = login.registerUser(firstName, lastName, username, password, cellPhoneNumber);
        System.out.println(registrationResult);

        // ---------- Login (Part 1) ----------
        System.out.println();
        System.out.println("=== Login ===");

        System.out.print("Enter your username: ");
        String loginUsername = scanner.nextLine();

        System.out.print("Enter your password: ");
        String loginPassword = scanner.nextLine();

        boolean loginSuccessful = login.loginUser(loginUsername, loginPassword);
        System.out.println(login.returnLoginStatus(loginSuccessful));

        // ---------- QuickChat (Parts 2 and 3) - only for users who logged in ----------
        if (loginSuccessful) {
            String sender = firstName + " " + lastName;
            runQuickChat(scanner, sender);
        }

        scanner.close();
    }

    // Shows the welcome message and the menu until the user quits
    public static void runQuickChat(Scanner scanner, String sender) {

        Message message = new Message();

        // Read the stored messages from the JSON file into the arrays
        message.readStoredMessages();

        System.out.println();
        System.out.println("Welcome to QuickChat.");

        // The user decides how many messages they want to enter
        int numMessages = 0;
        while (numMessages < 1) {
            System.out.print("How many messages would you like to enter? ");
            String input = scanner.nextLine().trim();

            if (input.matches("[0-9]{1,3}")) {
                numMessages = Integer.parseInt(input);
            }

            if (numMessages < 1) {
                System.out.println("Please enter a whole number greater than 0.");
            }
        }

        boolean messagesEntered = false;
        String menuChoice = "";

        while (!menuChoice.equals("3")) {
            System.out.println();
            System.out.println("1) Send Messages");
            System.out.println("2) Show recently sent messages");
            System.out.println("3) Quit");
            System.out.println("4) Stored Messages");
            System.out.print("Choose an option (1-4): ");
            menuChoice = scanner.nextLine().trim();

            if (menuChoice.equals("1")) {
                if (messagesEntered) {
                    System.out.println("You have already entered all " + numMessages + " of your messages.");
                } else {
                    sendMessages(scanner, message, numMessages);
                    messagesEntered = true;
                }
            } else if (menuChoice.equals("2")) {
                System.out.println("Coming Soon.");
            } else if (menuChoice.equals("3")) {
                System.out.println("Thank you for using QuickChat. Goodbye!");
            } else if (menuChoice.equals("4")) {
                storedMessagesMenu(scanner, message, sender);
            } else {
                System.out.println("Invalid option. Please enter 1, 2, 3 or 4.");
            }
        }
    }

    // Lets the user enter exactly numMessages messages
    public static void sendMessages(Scanner scanner, Message message, int numMessages) {

        for (int i = 0; i < numMessages; i++) {

            System.out.println();
            System.out.println("--- Message number " + i + " ---");

            // Message ID
            String messageID = message.generateMessageID();
            System.out.println(message.returnMessageIDText(messageID));

            // Recipient
            System.out.print("Enter the recipient's cell number (e.g. +27718693002): ");
            String recipient = scanner.nextLine().trim();

            while (!message.isRecipientValid(recipient)) {
                System.out.println(message.checkRecipientCell(recipient));
                System.out.print("Enter the recipient's cell number: ");
                recipient = scanner.nextLine().trim();
            }
            System.out.println(message.checkRecipientCell(recipient));

            // Message text
            System.out.print("Enter your message (max 250 characters): ");
            String text = scanner.nextLine();

            while (text.length() > 250) {
                System.out.println("Please enter a message of less than 250 characters.");
                System.out.print("Enter your message again: ");
                text = scanner.nextLine();
            }
            System.out.println("Message sent");

            // Message hash
            String messageHash = message.createMessageHash(messageID, i, text);

            // Send, disregard or store
            System.out.println("What would you like to do with this message?");
            System.out.println("1) Send Message");
            System.out.println("2) Disregard Message");
            System.out.println("3) Store Message to send later");
            System.out.print("Choose an option (1-3): ");
            String input = scanner.nextLine().trim();

            while (!input.equals("1") && !input.equals("2") && !input.equals("3")) {
                System.out.print("Invalid option. Please enter 1, 2 or 3: ");
                input = scanner.nextLine().trim();
            }

            int sendChoice = Integer.parseInt(input);
            System.out.println(message.SentMessage(sendChoice, messageID, messageHash, recipient, text));

            if (sendChoice == 1) {
                System.out.println(message.getMessageDetails(messageID, messageHash, recipient, text));
            } else if (sendChoice == 2) {
                String deleteInput = scanner.nextLine().trim();
                while (!deleteInput.equals("0")) {
                    System.out.print("Press 0 to delete the message: ");
                    deleteInput = scanner.nextLine().trim();
                }
                System.out.println("Message deleted.");
            }
        }

        // All messages have been entered, so show everything that was sent
        System.out.println();
        System.out.println("Messages sent:");
        System.out.println(message.printMessages());
        System.out.println("Total number of messages sent: " + message.returnTotalMessages());
    }

    // The Stored Messages menu (Part 3)
    public static void storedMessagesMenu(Scanner scanner, Message message, String sender) {

        String choice = "";

        while (!choice.equals("8")) {
            System.out.println();
            System.out.println("--- Stored Messages ---");
            System.out.println("1) Display all stored messages");
            System.out.println("2) Display the sender and recipient of all stored messages");
            System.out.println("3) Display the longest stored message");
            System.out.println("4) Search for a message ID");
            System.out.println("5) Search for all messages to a recipient");
            System.out.println("6) Delete a message using the message hash");
            System.out.println("7) Display the message report");
            System.out.println("8) Back to the main menu");
            System.out.print("Choose an option (1-8): ");
            choice = scanner.nextLine().trim();

            if (choice.equals("1")) {
                System.out.println(message.displayStoredMessages());
            } else if (choice.equals("2")) {
                System.out.println(message.displaySenderAndRecipient(sender));
            } else if (choice.equals("3")) {
                System.out.println(message.longestStoredMessage());
            } else if (choice.equals("4")) {
                System.out.print("Enter the message ID: ");
                String messageID = scanner.nextLine().trim();
                System.out.println(message.searchByMessageID(messageID));
            } else if (choice.equals("5")) {
                System.out.print("Enter the recipient's cell number: ");
                String recipient = scanner.nextLine().trim();
                System.out.println(message.searchByRecipient(recipient));
            } else if (choice.equals("6")) {
                System.out.print("Enter the message hash: ");
                String messageHash = scanner.nextLine().trim();
                System.out.println(message.deleteMessageByHash(messageHash));
            } else if (choice.equals("7")) {
                System.out.println(message.displayReport());
            } else if (choice.equals("8")) {
                System.out.println("Returning to the main menu.");
            } else {
                System.out.println("Invalid option. Please enter a number from 1 to 8.");
            }
        }
    }
}