# WhatsApp Automation Software

This project automates the process of sending messages to multiple contacts on WhatsApp. Users can load contacts automatically, select a contact, specify a message, and define the number of times the message should be sent. The software then sends the message the specified number of times to the chosen contact.

## Features
- **Automated Contact Loading**: Automatically load contacts from WhatsApp.
- **Message Customization**: Specify the message content.
- **Message Frequency**: Define how many times the message should be sent to the selected contact.

## Prerequisites
- Java 8 or later
- Selenium WebDriver
- ChromeDriver (or other WebDriver for your browser of choice)
  
## Installation

1. **Download the Project**:
   Download or manually upload the files from this repository to your local machine.

2. **Set up your WebDriver**:
   Download the appropriate WebDriver (e.g., ChromeDriver) and place it in the `drivers/` directory.

3. **Install Dependencies**:
   - If you're using Maven, run:
     ```
     mvn install

   - If you're using Gradle, run:
     ```
     gradle build
     ```

4. **Run the Application**:
   - Compile and run the main class from your IDE or command line:
     ```
     java -jar WhatsAppAutomation.jar
     ```

## Usage
1. Open the application.
2. Select the contact to which you want to send the message.
3. Enter the message you want to send.
4. Specify the number of times the message should be sent.
5. Click "Send" to start the automation.

