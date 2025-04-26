import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.StaleElementReferenceException;


import java.util.List;
import java.util.stream.Collectors;

public class ContactManager {
    private WebDriver driver;

    public ContactManager(WebDriver driver) {
        this.driver = driver;
    }

    /**
     * Retrieves a list of contact names visible on WhatsApp Web.
     *
     * @return A list of contact names.
     */
    public List<String> getContactNames() {
        try {
            // Use the correct XPath to select all contacts
           List<WebElement> contactElements = driver.findElements(By.xpath("//span[@title]"));


            List<String> contactNames = contactElements.stream()
                    .map(WebElement::getText)
                    .collect(Collectors.toList());

            System.out.println("Found " + contactNames.size() + " contacts.");
            return contactNames;
        } catch (Exception e) {
            System.out.println("Error retrieving contact names: " + e.getMessage());
            return List.of(); // Return an empty list if an error occurs
        }
    }

    /**
     * Finds and scrolls to a contact by its name.
     *
     * @param contactName The name of the contact to locate.
     * @return The WebElement of the contact if found, or null otherwise.
     */
    public WebElement getContactByName(String contactName) {
        try {
            System.out.println("Searching for contact: " + contactName);

            // Use the correct XPath to find a contact by name
            WebElement contact = driver.findElement(By.xpath("//span[contains(text(), '" + contactName + "')]"));

            // Scroll into view to ensure visibility
            scrollToElement(contact);

            System.out.println("Contact found: " + contactName);
            return contact;
        } catch (StaleElementReferenceException e) {
            System.out.println("Stale element exception while finding contact: " + contactName);
            return retryFindContact(contactName);
        } catch (Exception e) {
            System.out.println("Contact not found: " + contactName + ". Error: " + e.getMessage());
            return null;
        }
    }

    /**
     * Retries finding the contact in case of a stale element reference.
     *
     * @param contactName The name of the contact to locate.
     * @return The WebElement of the contact if found, or null otherwise.
     */
    private WebElement retryFindContact(String contactName) {
        try {
            System.out.println("Retrying to find contact: " + contactName);

            // Use the correct XPath to find a contact by name
            WebElement contact = driver.findElement(By.xpath("//span[contains(text(), '" + contactName + "')]"));

            // Scroll into view to ensure visibility
            scrollToElement(contact);

            System.out.println("Contact successfully found on retry: " + contactName);
            return contact;
        } catch (Exception e) {
            System.out.println("Retry failed for contact: " + contactName + ". Error: " + e.getMessage());
            return null;
        }
    }

    /**
     * Scrolls the specified WebElement into view using JavaScript.
     *
     * @param element The WebElement to scroll into view.
     */
    private void scrollToElement(WebElement element) {
        try {
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
            Thread.sleep(500); // Small delay to ensure smooth scrolling
        } catch (Exception e) {
            System.out.println("Error scrolling to element: " + e.getMessage());
        }
    }
}

