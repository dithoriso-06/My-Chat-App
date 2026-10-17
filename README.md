# QuickChat – Part 2: Sending Messages

A console-based chat application written in Java. Users register and log in (Part 1), and once logged in they can enter and send messages (Part 2). There is no GUI.

## Features
- Only users who log in successfully can use QuickChat.
- Shows the welcome message "Welcome to QuickChat." and a numeric menu:
  1. Send Messages
  2. Show recently sent messages (displays "Coming Soon.")
  3. Quit
- The app runs in a while loop until the user chooses Quit.
- The user chooses how many messages to enter, and a for loop allows exactly that many.
- Each message has:
  - **Message ID:** a random ten-digit number, built with a loop and `substring`.
  - **Message number:** the loop counter, starting at 0.
  - **Recipient:** must start with the international code +27 and have no more than ten digits after it (checked with a regular expression).
  - **Message:** no more than 250 characters.
  - **Message hash:** the first two digits of the ID, the message number, and the first and last words of the message, in capitals (for example `00:0:HITONIGHT`).
- For each message the user can send it, disregard it, or store it to send later.
- Stored messages are saved in a JSON file called `messages.json`.
- When all messages are entered, the sent messages and the total number sent are displayed. Only sent messages count towards the total.

## Project files
- `src/main/java/Login.java` – registration and login logic (Part 1).
- `src/main/java/Message.java` – creates, checks, sends, stores and prints messages.
- `src/main/java/Main.java` – the console menu and program flow.
- `src/test/java/LoginTest.java` – unit tests for the Login class.
- `src/test/java/MessageTest.java` – unit tests for the Message class.
- `.github/workflows/TestJava.yml` – GitHub Actions workflow that runs all tests on every push.
- `pom.xml` – Maven configuration (Java 17, JUnit 5).

## How to run
1. Open the project in NetBeans as a Maven project.
2. Run `Main.java`.
3. Register, then log in with the same username and password.
4. Use the menu to send messages.

## How to run the tests
- In NetBeans, right-click the project and choose Test, or run `mvn test`.
- The tests also run automatically on GitHub through GitHub Actions each time code is pushed.

## Methods in the Message class
- `generateMessageID()` – creates the random ten-digit message ID.
- `checkMessageID()` – checks the ID is not more than ten characters.
- `checkMessageLength()` – checks the message is 250 characters or less.
- `checkRecipientCell()` – checks the recipient's number has the international code.
- `createMessageHash()` – creates the message hash.
- `SentMessage()` – sends, disregards or stores a message.
- `printMessages()` – returns all messages sent while the program is running.
- `returnTotalMessages()` – returns the total number of messages sent.
- `storeMessage()` – saves a message to the JSON file.

## References
- Ecma International (n.d.) *Introducing JSON*. Available at: https://www.json.org/json-en.html
- Oracle (n.d.) *FileWriter (Java SE 17 & JDK 17)*. Available at: https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/io/FileWriter.html
- Oracle (n.d.) *Lesson: Regular Expressions*. Available at: https://docs.oracle.com/javase/tutorial/essential/regex/
