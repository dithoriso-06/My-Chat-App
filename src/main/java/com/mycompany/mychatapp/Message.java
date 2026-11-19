/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mychatapp;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

/**
 *
 * @author Student
 */
public class Message {
     private String fileName;                        // the JSON file the stored messages are saved in

    // Parallel arrays: the same index in each array belongs to the same message
    private String[] messageIDs = new String[1000];     // contains all the message IDs
    private String[] messageHashes = new String[1000];  // contains all the message hashes
    private String[] recipients = new String[1000];
    private String[] messages = new String[1000];
    private String[] flags = new String[1000];          // "Sent", "Disregarded" or "Stored"
    private int messageCount = 0;

    // Arrays that contain only the messages with each flag
    private String[] sentMessages = new String[1000];
    private String[] disregardedMessages = new String[1000];
    private String[] storedMessages = new String[1000];
    private int sentCount = 0;
    private int disregardedCount = 0;
    private int storedCount = 0;

    // Normal constructor: stores messages in messages.json
    public Message() {
        this.fileName = "messages.json";
    }

    // Constructor that lets the unit tests use a different file
    public Message(String fileName) {
        this.fileName = fileName;
    }

    // Creates a random ten digit message ID by cutting a longer number with substring
    public String generateMessageID() {
        Random random = new Random();
        long number = 1000000000000L + (long) (random.nextDouble() * 9000000000000L);
        String fullNumber = String.valueOf(number);
        return fullNumber.substring(0, 10);
    }

    // Returns the text shown when a message ID has been created
    public String returnMessageIDText(String messageID) {
        return "Message ID generated: " + messageID;
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

        String text = message.trim();
        int firstSpace = text.indexOf(" ");
        int lastSpace = text.lastIndexOf(" ");

        String firstWord;
        String lastWord;

        if (firstSpace == -1) {
            // The message is only one word
            firstWord = text;
            lastWord = text;
        } else {
            firstWord = text.substring(0, firstSpace);
            lastWord = text.substring(lastSpace + 1);
        }

        // Remove punctuation such as commas and question marks
        firstWord = firstWord.replaceAll("[^a-zA-Z0-9]", "");
        lastWord = lastWord.replaceAll("[^a-zA-Z0-9]", "");

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

    // Adds a message to the arrays. The flag is "Sent", "Disregarded" or "Stored"
    public void addMessage(String messageID, String messageHash, String recipient,
                           String message, String flag) {

        if (messageCount == messageIDs.length) {
            return;     // the arrays are full
        }

        messageIDs[messageCount] = messageID;
        messageHashes[messageCount] = messageHash;
        recipients[messageCount] = recipient;
        messages[messageCount] = message;
        flags[messageCount] = flag;
        messageCount = messageCount + 1;

        updateMessageArrays();
    }

    // Fills the sent, disregarded and stored arrays using the flag of each message
    private void updateMessageArrays() {
        sentCount = 0;
        disregardedCount = 0;
        storedCount = 0;

        for (int i = 0; i < messageCount; i++) {
            if (flags[i].equals("Sent")) {
                sentMessages[sentCount] = messages[i];
                sentCount = sentCount + 1;
            } else if (flags[i].equals("Disregarded")) {
                disregardedMessages[disregardedCount] = messages[i];
                disregardedCount = disregardedCount + 1;
            } else if (flags[i].equals("Stored")) {
                storedMessages[storedCount] = messages[i];
                storedCount = storedCount + 1;
            }
        }
    }

    // Simple getters used to check the arrays
    public String getSentMessage(int index) {
        return sentMessages[index];
    }

    public String getDisregardedMessage(int index) {
        return disregardedMessages[index];
    }

    public String getStoredMessage(int index) {
        return storedMessages[index];
    }

    public String getMessageID(int index) {
        return messageIDs[index];
    }

    public String getMessageHash(int index) {
        return messageHashes[index];
    }

    // choice 1 = send, 2 = disregard, 3 = store
    public String SentMessage(int choice, String messageID, String messageHash,
                              String recipient, String message) {

        if (choice == 1) {
            addMessage(messageID, messageHash, recipient, message, "Sent");
            return "Message successfully sent.";
        } else if (choice == 2) {
            addMessage(messageID, messageHash, recipient, message, "Disregarded");
            return "Press 0 to delete the message.";
        } else if (choice == 3) {
            addMessage(messageID, messageHash, recipient, message, "Stored");
            storeMessage();
            return "Message successfully stored.";
        } else {
            return "Invalid option.";
        }
    }

    // Returns the details of all the messages that were sent while the program is running
    public String printMessages() {
        if (sentCount == 0) {
            return "No messages have been sent.";
        }

        String result = "";
        for (int i = 0; i < messageCount; i++) {
            if (flags[i].equals("Sent")) {
                result = result + getMessageDetails(messageIDs[i], messageHashes[i],
                        recipients[i], messages[i]) + "\n";
            }
        }
        return result;
    }

    // Returns the total number of messages sent
    public int returnTotalMessages() {
        return sentCount;
    }

    // Returns all the stored messages (these are read from the JSON file when the app starts)
    public String displayStoredMessages() {
        if (storedCount == 0) {
            return "No stored messages.";
        }

        String result = "";
        for (int i = 0; i < storedCount; i++) {
            result = result + (i + 1) + ". " + storedMessages[i] + "\n";
        }
        return result;
    }

    // Returns the sender and the recipient of every stored message
    public String displaySenderAndRecipient(String sender) {
        String result = "";
        for (int i = 0; i < messageCount; i++) {
            if (flags[i].equals("Stored")) {
                result = result + "Sender: " + sender + ", Recipient: " + recipients[i] + "\n";
            }
        }

        if (result.equals("")) {
            return "No stored messages.";
        }
        return result;
    }

    // Returns the longest stored message
    public String longestStoredMessage() {
        if (storedCount == 0) {
            return "No stored messages.";
        }

        String longest = storedMessages[0];
        for (int i = 1; i < storedCount; i++) {
            if (storedMessages[i].length() > longest.length()) {
                longest = storedMessages[i];
            }
        }
        return longest;
    }

    // Searches for a message ID and returns the recipient and the message
    public String searchByMessageID(String messageID) {
        for (int i = 0; i < messageCount; i++) {
            if (messageIDs[i].equals(messageID)) {
                return "Recipient: " + recipients[i] + ", Message: " + messages[i];
            }
        }
        return "Message ID not found.";
    }

    // Returns all the sent and stored messages for one recipient
    public String searchByRecipient(String recipient) {
        String result = "";
        for (int i = 0; i < messageCount; i++) {
            if (recipients[i].equals(recipient)
                    && (flags[i].equals("Sent") || flags[i].equals("Stored"))) {
                result = result + messages[i] + "\n";
            }
        }

        if (result.equals("")) {
            return "No messages found for this recipient.";
        }
        return result;
    }

    // Deletes a message using its message hash
    public String deleteMessageByHash(String messageHash) {
        for (int i = 0; i < messageCount; i++) {
            if (messageHashes[i].equalsIgnoreCase(messageHash)) {

                String deletedMessage = messages[i];
                String deletedFlag = flags[i];

                // Move every message after this one up by one place
                for (int j = i; j < messageCount - 1; j++) {
                    messageIDs[j] = messageIDs[j + 1];
                    messageHashes[j] = messageHashes[j + 1];
                    recipients[j] = recipients[j + 1];
                    messages[j] = messages[j + 1];
                    flags[j] = flags[j + 1];
                }
                messageCount = messageCount - 1;
                updateMessageArrays();

                // Keep the JSON file up to date if a stored message was deleted
                if (deletedFlag.equals("Stored")) {
                    storeMessage();
                }

                return "Message: \"" + deletedMessage + "\" successfully deleted.";
            }
        }
        return "Message hash not found.";
    }

    // Returns a report with the full details of all the sent and stored messages
    public String displayReport() {
        String report = "===== Message Report =====\n\n";

        report = report + "--- Sent Messages ---\n";
        if (sentCount == 0) {
            report = report + "None\n";
        }
        for (int i = 0; i < messageCount; i++) {
            if (flags[i].equals("Sent")) {
                report = report + getMessageDetails(messageIDs[i], messageHashes[i],
                        recipients[i], messages[i]) + "\n";
            }
        }

        report = report + "--- Stored Messages ---\n";
        if (storedCount == 0) {
            report = report + "None\n";
        }
        for (int i = 0; i < messageCount; i++) {
            if (flags[i].equals("Stored")) {
                report = report + getMessageDetails(messageIDs[i], messageHashes[i],
                        recipients[i], messages[i]) + "\n";
            }
        }

        return report;
    }

    // Saves all the stored messages to a JSON file.
    // Reference: Google (n.d.) Gson User Guide, https://github.com/google/gson/blob/main/UserGuide.md
    // Reference: Ecma International (n.d.) Introducing JSON, https://www.json.org/json-en.html
    public void storeMessage() {
        JsonArray jsonArray = new JsonArray();

        for (int i = 0; i < messageCount; i++) {
            if (flags[i].equals("Stored")) {
                JsonObject jsonMessage = new JsonObject();
                jsonMessage.addProperty("messageID", messageIDs[i]);
                jsonMessage.addProperty("messageHash", messageHashes[i]);
                jsonMessage.addProperty("recipient", recipients[i]);
                jsonMessage.addProperty("message", messages[i]);
                jsonArray.add(jsonMessage);
            }
        }

        try {
            FileWriter writer = new FileWriter(fileName);
            Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
            gson.toJson(jsonArray, writer);
            writer.close();
        } catch (IOException e) {
            System.out.println("The message could not be stored: " + e.getMessage());
        }
    }

    // Reads the stored messages from the JSON file into the arrays.
    // Reference: Google (n.d.) Gson User Guide, https://github.com/google/gson/blob/main/UserGuide.md
    // Reference: Oracle (n.d.) FileReader, https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/io/FileReader.html
    public void readStoredMessages() {
        File file = new File(fileName);

        if (!file.exists()) {
            return;     // nothing has been stored yet
        }

        try {
            FileReader reader = new FileReader(file);
            JsonArray jsonArray = JsonParser.parseReader(reader).getAsJsonArray();
            reader.close();

            for (int i = 0; i < jsonArray.size(); i++) {
                JsonObject jsonMessage = jsonArray.get(i).getAsJsonObject();

                String messageID = jsonMessage.get("messageID").getAsString();
                String messageHash = jsonMessage.get("messageHash").getAsString();
                String recipient = jsonMessage.get("recipient").getAsString();
                String message = jsonMessage.get("message").getAsString();

                addMessage(messageID, messageHash, recipient, message, "Stored");
            }
        } catch (Exception e) {
            System.out.println("The stored messages could not be read: " + e.getMessage());
        }
    }
}