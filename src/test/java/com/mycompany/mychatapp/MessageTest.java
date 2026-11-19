/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.mycompany.mychatapp;

import java.io.File;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Student
 */
public class MessageTest {
    
     // Adds one message using a developer entry (no hard-coding in the app itself)
    private void addTestMessage(Message message, String id, int number,
                                String recipient, String text, String flag) {
        String hash = message.createMessageHash(id, number, text);
        message.addMessage(id, hash, recipient, text, flag);
    }

    // Adds the five test messages from the brief
    private Message createTestMessages() {
        Message message = new Message("testMessages.json");
        addTestMessage(message, "1000000001", 0, "+27834557896", "Did you get the cake?", "Sent");
        addTestMessage(message, "1000000002", 1, "+27838884567",
                "Where are you? You are late! I have asked you to be on time.", "Stored");
        addTestMessage(message, "1000000003", 2, "+27834484567", "Yohoooo, I am at your gate.", "Disregarded");
        addTestMessage(message, "1000000004", 3, "0838884567", "It is dinner time!", "Sent");
        addTestMessage(message, "1000000005", 4, "+27838884567", "Ok, I am leaving without you.", "Stored");
        return message;
    }

    // ---------- Part 2 tests ----------

    @Test
    public void testMessageLengthSuccess() {
        Message message = new Message();
        assertEquals("Message ready to send.",
                message.checkMessageLength("Hi Mike, can you join us for dinner tonight?"));
    }

    @Test
    public void testMessageLengthFailure() {
        Message message = new Message();
        String longMessage = "a".repeat(260);
        assertEquals("Message exceeds 250 characters by 10; please reduce the size.",
                message.checkMessageLength(longMessage));
    }

    @Test
    public void testRecipientCorrectlyFormatted() {
        Message message = new Message();
        assertEquals("Cell phone number successfully captured.",
                message.checkRecipientCell("+27718693002"));
    }

    @Test
    public void testRecipientIncorrectlyFormatted() {
        Message message = new Message();
        assertEquals("Cell phone number is incorrectly formatted or does not contain an "
                + "international code. Please correct the number and try again.",
                message.checkRecipientCell("08575975889"));
    }

    @Test
    public void testMessageHashes() {
        Message message = new Message();

        // Loop through both test cases: i is the message number
        for (int i = 0; i < 2; i++) {
            String messageID;
            String text;
            String expectedHash;

            if (i == 0) {
                messageID = "0012345678";
                text = "Hi Mike, can you join us for dinner tonight?";
                expectedHash = "00:0:HITONIGHT";
            } else {
                messageID = "1234567890";
                text = "Hi Keegan, did you receive the payment?";
                expectedHash = "12:1:HIPAYMENT";
            }

            assertEquals(expectedHash, message.createMessageHash(messageID, i, text));
        }
    }

    @Test
    public void testMessageIDCreated() {
        Message message = new Message();
        String messageID = message.generateMessageID();

        assertEquals(10, messageID.length());
        assertTrue(messageID.matches("[0-9]{10}"));
        assertTrue(message.checkMessageID(messageID));
        assertEquals("Message ID generated: " + messageID, message.returnMessageIDText(messageID));
    }

    @Test
    public void testMessageIDTooLong() {
        Message message = new Message();
        assertFalse(message.checkMessageID("12345678901"));
    }

    @Test
    public void testSendMessage() {
        Message message = new Message();
        String result = message.SentMessage(1, "0012345678", "00:0:HITONIGHT",
                "+27718693002", "Hi Mike, can you join us for dinner tonight?");
        assertEquals("Message successfully sent.", result);
    }

    @Test
    public void testDisregardMessage() {
        Message message = new Message();
        String result = message.SentMessage(2, "1234567890", "12:1:HIPAYMENT",
                "08575975889", "Hi Keegan, did you receive the payment?");
        assertEquals("Press 0 to delete the message.", result);
    }

    @Test
    public void testStoreMessage() {
        Message message = new Message("testMessages.json");
        File file = new File("testMessages.json");
        file.delete();

        String result = message.SentMessage(3, "0012345678", "00:0:HITONIGHT",
                "+27718693002", "Hi Mike, can you join us for dinner tonight?");

        assertEquals("Message successfully stored.", result);
        assertTrue(file.exists());
    }

    @Test
    public void testReturnTotalMessages() {
        Message message = new Message();
        message.SentMessage(1, "0012345678", "00:0:HITONIGHT",
                "+27718693002", "Hi Mike, can you join us for dinner tonight?");
        message.SentMessage(2, "1234567890", "12:1:HIPAYMENT",
                "08575975889", "Hi Keegan, did you receive the payment?");

        assertEquals(1, message.returnTotalMessages());
    }

    @Test
    public void testPrintMessages() {
        Message message = new Message();
        message.SentMessage(1, "0012345678", "00:0:HITONIGHT",
                "+27718693002", "Hi Mike, can you join us for dinner tonight?");
        message.SentMessage(2, "1234567890", "12:1:HIPAYMENT",
                "08575975889", "Hi Keegan, did you receive the payment?");

        String printed = message.printMessages();
        assertTrue(printed.contains("Hi Mike, can you join us for dinner tonight?"));
        assertFalse(printed.contains("Hi Keegan"));
    }

    // ---------- Part 3 tests (test data from the brief) ----------

    @Test
    public void testSentMessagesArrayPopulated() {
        Message message = createTestMessages();

        assertEquals("Did you get the cake?", message.getSentMessage(0));
        assertEquals("It is dinner time!", message.getSentMessage(1));
        assertEquals(2, message.returnTotalMessages());
    }

    @Test
    public void testDisregardedMessagesArrayPopulated() {
        Message message = createTestMessages();

        assertEquals("Yohoooo, I am at your gate.", message.getDisregardedMessage(0));
    }

    @Test
    public void testStoredMessagesArrayPopulated() {
        Message message = createTestMessages();

        assertEquals("Where are you? You are late! I have asked you to be on time.",
                message.getStoredMessage(0));
        assertEquals("Ok, I am leaving without you.", message.getStoredMessage(1));
    }

    @Test
    public void testMessageIDAndHashArraysPopulated() {
        Message message = createTestMessages();

        assertEquals("1000000001", message.getMessageID(0));
        assertEquals("1000000005", message.getMessageID(4));
        assertEquals("10:0:DIDCAKE", message.getMessageHash(0));
        assertEquals("10:1:WHERETIME", message.getMessageHash(1));
        assertEquals("10:2:YOHOOOOGATE", message.getMessageHash(2));
        assertEquals("10:3:ITTIME", message.getMessageHash(3));
        assertEquals("10:4:OKYOU", message.getMessageHash(4));
    }

    @Test
    public void testDisplaySenderAndRecipient() {
        Message message = createTestMessages();

        assertEquals("Sender: Kyle Smith, Recipient: +27838884567\n"
                + "Sender: Kyle Smith, Recipient: +27838884567\n",
                message.displaySenderAndRecipient("Kyle Smith"));
    }

    @Test
    public void testDisplayLongestMessage() {
        Message message = createTestMessages();

        assertEquals("Where are you? You are late! I have asked you to be on time.",
                message.longestStoredMessage());
    }

    @Test
    public void testSearchForMessageID() {
        Message message = createTestMessages();

        assertEquals("Recipient: 0838884567, Message: It is dinner time!",
                message.searchByMessageID("1000000004"));
    }

    @Test
    public void testSearchForMessageIDNotFound() {
        Message message = createTestMessages();

        assertEquals("Message ID not found.", message.searchByMessageID("9999999999"));
    }

    @Test
    public void testSearchByRecipient() {
        Message message = createTestMessages();

        assertEquals("Where are you? You are late! I have asked you to be on time.\n"
                + "Ok, I am leaving without you.\n",
                message.searchByRecipient("+27838884567"));
    }

    @Test
    public void testSearchByRecipientNotFound() {
        Message message = createTestMessages();

        assertEquals("No messages found for this recipient.",
                message.searchByRecipient("+27000000000"));
    }

    @Test
    public void testDeleteMessageUsingHash() {
        Message message = createTestMessages();

        assertEquals("Message: \"Where are you? You are late! I have asked you to be on time.\" "
                + "successfully deleted.", message.deleteMessageByHash("10:1:WHERETIME"));

        // The message is gone from the arrays
        assertEquals("Ok, I am leaving without you.", message.getStoredMessage(0));
        assertEquals("Ok, I am leaving without you.\n", message.searchByRecipient("+27838884567"));
    }

    @Test
    public void testDeleteMessageHashNotFound() {
        Message message = createTestMessages();

        assertEquals("Message hash not found.", message.deleteMessageByHash("00:0:NOTHERE"));
    }

    @Test
    public void testDisplayReport() {
        Message message = createTestMessages();
        String report = message.displayReport();

        // A sent message
        assertTrue(report.contains("10:0:DIDCAKE"));
        assertTrue(report.contains("+27834557896"));
        assertTrue(report.contains("Did you get the cake?"));

        // A stored message
        assertTrue(report.contains("10:1:WHERETIME"));
        assertTrue(report.contains("+27838884567"));
        assertTrue(report.contains("Where are you? You are late! I have asked you to be on time."));

        // A disregarded message is not in the report
        assertFalse(report.contains("Yohoooo"));
    }

    @Test
    public void testStoreAndReadJsonFile() {
        Message message = createTestMessages();
        message.storeMessage();

        // A new Message object reads the stored messages from the JSON file
        Message newMessage = new Message("testMessages.json");
        newMessage.readStoredMessages();

        assertEquals("Where are you? You are late! I have asked you to be on time.",
                newMessage.getStoredMessage(0));
        assertEquals("Ok, I am leaving without you.", newMessage.getStoredMessage(1));
        assertEquals("10:1:WHERETIME", newMessage.getMessageHash(0));
    }
}