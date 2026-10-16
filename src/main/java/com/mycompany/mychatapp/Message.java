/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mychatapp;

/**
 *
 * @author Student
 */
public class Message {
    private int totalMessages = 0;      // number of messages that were sent
    private String allMessages = "";    // details of every sent message
    private String storedJson = "";     // JSON text of every stored message

    // Creates a random ten digit message ID, one digit at a time
    public String generateMessageID() {
        Random random = new Random();
        String messageID = "";

        for (int i = 0; i < 10; i++) {
            messageID = messageID + random.nextInt(10);
        }

        return messageID;
    }

    // Checks that the message ID is not more than ten characters
    public boolean checkMessageID(String messageID) {
        return messageID != null && messageID.length() <= 10;
    }

    // Checks that the message is not more than 250 characters
    public String checkMessageLength(String message) {
        if (message.length() <= 250) {
            return "Message ready to send.";
        } else {
            int extra = message.length() - 250;
            return "Message exceeds 250 characters by " + extra + "; please reduce the size.";
        }
    }

    // Checks the cell number starts with +27 followed by up to ten digits
    public boolean isRecipientValid(String recipient) {
        return recipient != null && recipient.matches("^\\+27[0-9]{1,10}$");
    }

    // Returns the correct message for the recipient's cell number
    public String checkRecipientCell(String recipient) {
        if (isRecipientValid(recipient)) {
            return "Cell phone number successfully captured.";
        } else {
            return "Cell phone number is incorrectly formatted or does not contain an "
                    + "international code. Please correct the number and try again.";
        }
    }

    // Creates the hash: first two digits of ID : message number : first word + last word
    public String createMessageHash(String messageID, int messageNumber, String message) {
        String firstTwo = messageID.substring(0, 2);

        String[] words = message.trim().split(" ");
        String firstWord = words[0].replaceAll("[^a-zA-Z0-9]", "");
        String lastWord = words[words.length - 1].replaceAll("[^a-zA-Z0-9]", "");

        return (firstTwo + ":" + messageNumber + ":" + firstWord + lastWord).toUpperCase();
    }

    // Lays out the details of one message in the required order
    public String getMessageDetails(String messageID, String messageHash,
                                    String recipient, String message) {
        return "Message ID: " + messageID + "\n"
                + "Message Hash: " + messageHash + "\n"
                + "Recipient: " + recipient + "\n"
                + "Message: " + message + "\n";
    }

    // choice 1 = send, 2 = disregard, 3 = store
    public String SentMessage(int choice, String messageID, String messageHash,
                              String recipient, String message) {

        if (choice == 1) {
            totalMessages = totalMessages + 1;
            allMessages = allMessages + getMessageDetails(messageID, messageHash, recipient, message) + "\n";
            return "Message successfully sent.";
        } else if (choice == 2) {
            return "Press 0 to delete the message.";
        } else if (choice == 3) {
            storeMessage(messageID, messageHash, recipient, message);
            return "Message successfully stored.";
        } else {
            return "Invalid option.";
        }
    }

    // Returns all the messages that were sent while the program is running
    public String printMessages() {
        if (allMessages.equals("")) {
            return "No messages have been sent.";
        }
        return allMessages;
    }

    // Returns the total number of messages sent
    public int returnTotalMessages() {
        return totalMessages;
    }

    // Stores the message in a JSON file called messages.json
    public void storeMessage(String messageID, String messageHash,
                             String recipient, String message) {

        // Escape backslashes and quotes so the JSON stays valid
        String safeMessage = message.replace("\\", "\\\\").replace("\"", "\\\"");

        String entry = "  {\n"
                + "    \"messageID\": \"" + messageID + "\",\n"
                + "    \"messageHash\": \"" + messageHash + "\",\n"
                + "    \"recipient\": \"" + recipient + "\",\n"
                + "    \"message\": \"" + safeMessage + "\"\n"
                + "  }";

        if (storedJson.equals("")) {
            storedJson = entry;
        } else {
            storedJson = storedJson + ",\n" + entry;
        }

        try {
            FileWriter writer = new FileWriter("messages.json");
            writer.write("[\n" + storedJson + "\n]");
            writer.close();
        } catch (IOException e) {
            System.out.println("The message could not be stored: " + e.getMessage());
        }
    }
}
    

