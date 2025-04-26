import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;


import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class TextMessageSender extends MessageSender {
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1); // Executor for scheduling tasks

    public TextMessageSender(WebDriver driver, ContactManager contactManager) {
        super(driver, contactManager);
    }

    @Override
    public void sendMessage(String contactName, String message) {
        sendMessage(contactName, message, 0);  // Default to 0 for no interval (one-time message)
    }

    /**
     * Sends a message to a contact. If interval > 0, it will send the message at regular intervals.
     *
     * @param contactName The name of the contact to send the message to.
     * @param message The message to send.
     * @param intervalInSeconds The interval (in seconds) between each message. If 0, sends the message once.
     */
    public void sendMessage(String contactName, String message, int intervalInSeconds) {
        try {
            WebElement contact = contactManager.getContactByName(contactName);
            if (contact != null) {
                contact.click();

                // Wait for the message box to become visible
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
                WebElement messageBox = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='main']/footer/div[1]/div/span/div/div[2]/div[1]/div[2]/div[1]/p")));

                if (messageBox != null) {
                    messageBox.click();
                    messageBox.sendKeys(message);

                    // Send the message
                    WebElement sendButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[data-testid='compose-btn-send']")));
                    if (sendButton != null) {
                        sendButton.click();
                        System.out.println("Message sent successfully to: " + contactName);

                        // If an interval is specified, schedule the message to be sent at regular intervals
                        if (intervalInSeconds > 0) {
                            scheduler.scheduleAtFixedRate(() -> sendMessageToContact(contactName, message), 0, intervalInSeconds, TimeUnit.SECONDS);
                            System.out.println("Automated messages will be sent every " + intervalInSeconds + " seconds.");
                        }
                    } else {
                        System.out.println("Send button not found.");
                    }
                } else {
                    System.out.println("Message box not found.");
                }
            } else {
                System.out.println("Contact not found: " + contactName);
            }
        } catch (Exception e) {
            System.out.println("Failed to send message to " + contactName + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Helper method to send the message to the contact.
     * This is used for sending messages repeatedly at scheduled intervals.
     *
     * @param contactName The name of the contact to send the message to.
     * @param message The message to send.
     */
    private void sendMessageToContact(String contactName, String message) {
        try {
            WebElement contact = contactManager.getContactByName(contactName);
            if (contact != null) {
                contact.click();

                // Wait for the message box to become visible
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
                WebElement messageBox = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='main']/footer/div[1]/div/span/div/div[2]/div[1]/div[2]/div[1]/p")));

                if (messageBox != null) {
                    messageBox.click();
                    messageBox.sendKeys(message);

                    // Send the message
                    WebElement sendButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[data-testid='compose-btn-send']")));
                    if (sendButton != null) {
                        sendButton.click();
                        System.out.println("Automated message sent to: " + contactName);
                    } else {
                        System.out.println("Send button not found.");
                    }
                } else {
                    System.out.println("Message box not found.");
                }
            } else {
                System.out.println("Contact not found: " + contactName);
            }
        } catch (Exception e) {
            System.out.println("Failed to send automated message to " + contactName + ": " + e.getMessage());
            e.printStackTrace();
        }
    }
}

