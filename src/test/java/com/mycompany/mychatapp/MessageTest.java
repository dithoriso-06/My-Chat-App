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
        Message message = new Message();
        File file = new File("messages.json");
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
}