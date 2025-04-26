import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.io.File;
import java.time.Duration;

public class MultimediaMessageSender extends MessageSender {

    public MultimediaMessageSender(WebDriver driver, ContactManager contactManager) {
        super(driver, contactManager);
    }

    @Override
    public void sendMessage(String contactName, String filePath) {
        try {
            // Ensure the file exists before attempting to send
            File file = new File(filePath);
            if (!file.exists()) {
                System.out.println("File not found: " + filePath);
                return;
            }

            WebElement contact = contactManager.getContactByName(contactName);
            if (contact != null) {
                contact.click();
                Thread.sleep(2000); // Allow time for the chat to load

                // Wait for the attach button to be clickable
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
                WebElement attachButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='main']/footer/div[1]/div/span/div/div[1]/div/button/span")));
                if (attachButton != null) {
                    attachButton.click();
                    Thread.sleep(1000); // Allow time for the attach options to load

                    // Wait for the file input element to be visible and interactable
                    WebElement fileInput = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("input[type='file']")));
                    if (fileInput != null) {
                        fileInput.sendKeys(file.getAbsolutePath());
                        Thread.sleep(2000); // Allow time for the file to upload

                        // Wait for the send button to be clickable
                        WebElement sendButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("span[data-testid='send']")));
                        if (sendButton != null) {
                            sendButton.click();
                            System.out.println("Media sent successfully to: " + contactName);
                        } else {
                            System.out.println("Send button not found.");
                        }
                    } else {
                        System.out.println("File input not found.");
                    }
                } else {
                    System.out.println("Attach button not found.");
                }
            } else {
                System.out.println("Contact not found: " + contactName);
            }
        } catch (Exception e) {
            System.out.println("Failed to send media to " + contactName + ": " + e.getMessage());
            e.printStackTrace();
        }
    }
}

