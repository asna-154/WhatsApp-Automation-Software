import org.openqa.selenium.WebDriver;

public abstract class MessageSender {
    protected WebDriver driver;
    protected ContactManager contactManager;

    public MessageSender(WebDriver driver, ContactManager contactManager) {
        this.driver = driver;
        this.contactManager = contactManager;
    }

    /**
     * Sends a message to a contact.
     *
     * @param contactName The name of the contact.
     * @param messageOrPath The message or media file path to send.
     */
    public abstract void sendMessage(String contactName, String messageOrPath);

    /**
     * This method can be used for automated messages, which could be
     * scheduled or triggered by other components.
     * 
     * @param contactName The name of the contact.
     * @param message The message to send.
     */
    public void sendAutomatedMessage(String contactName, String message) {
        // Log the automated message action
        System.out.println("Sending automated message to: " + contactName);
        sendMessage(contactName, message);
    }
}

