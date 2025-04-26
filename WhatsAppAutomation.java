import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Executors;


import java.time.Duration;

public class WhatsAppAutomation {
    private WebDriver driver;

    public WhatsAppAutomation() {
        try {
            System.setProperty("webdriver.chrome.driver", "D:\\java project\\chromedriver-win64\\chromedriver.exe");
            driver = new ChromeDriver();
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
            driver.manage().window().maximize();
            driver.get("https://web.whatsapp.com");

            // Wait for WhatsApp Web to load and QR code to be visible (if not logged in)
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("canvas[aria-label='Scan me!']")));  // QR code

            // Wait for the chat list to be visible after QR code is scanned (indicating user is logged in)
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div[title='Chat list']")));
            System.out.println("Logged in successfully.");
        } catch (Exception e) {
            System.out.println("Error initializing WebDriver: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Sends an automated text message to a contact.
     *
     * @param contactName The name of the contact.
     * @param message The message to send.
     */
    public void sendAutomatedMessage(String contactName, String message) {
        try {
            // Create an instance of TextMessageSender (or MultimediaMessageSender if needed)
            TextMessageSender messageSender = new TextMessageSender(driver, new ContactManager(driver));

            // Send the message
            messageSender.sendMessage(contactName, message);
            System.out.println("Automated message sent to: " + contactName);
        } catch (Exception e) {
            System.out.println("Error sending automated message: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void closeDriver() {
        if (driver != null) {
            driver.quit();
        }
    }

    public WebDriver getDriver() {
        return driver;
    }
}
