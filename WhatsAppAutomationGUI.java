import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.List;
import org.openqa.selenium.WebDriver;
import java.util.concurrent.ScheduledExecutorService;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;

public class WhatsAppAutomationGUI {
    private JFrame frame;
    private JPanel contactPanel;
    private WebDriver driver;
    private ContactManager contactManager;
    private String selectedContact;
    private ScheduledExecutorService scheduler;
    private int messageCount = 0; // Counter to track number of messages sent

    public WhatsAppAutomationGUI() {
        initializeGUI();
        initializeWebDriver();
        scheduler = Executors.newScheduledThreadPool(1);  // Initialize the scheduler for automated messages
    }

    private void initializeGUI() {
        frame = new JFrame("WhatsApp Automation");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        // Main Panel Setup
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));

        // Left Panel (Contact List)
        JPanel leftPanel = new JPanel(new BorderLayout());
        JLabel contactsLabel = new JLabel("Contacts", SwingConstants.CENTER);
        leftPanel.add(contactsLabel, BorderLayout.NORTH);

        contactPanel = new JPanel(new GridLayout(0, 1, 5, 5));
        JScrollPane scrollPane = new JScrollPane(contactPanel);
        leftPanel.add(scrollPane, BorderLayout.CENTER);
        leftPanel.setPreferredSize(new java.awt.Dimension(300, 0));

        // Right Panel (Action Buttons)
        JPanel rightPanel = new JPanel(new BorderLayout(10, 10));
        JButton loadContactsButton = new JButton("Load Contacts");
        rightPanel.add(loadContactsButton, BorderLayout.NORTH);

        JPanel actionPanel = new JPanel(new GridLayout(6, 1, 10, 10)); // Increased rows for additional field
        JTextField messageField = new JTextField(20);
        JButton sendTextButton = new JButton("Send Text");
        JButton sendMediaButton = new JButton("Send Media");
        JTextField intervalField = new JTextField(20);  // Input for interval in seconds
        JTextField messageCountField = new JTextField(20); // Input for how many messages to send
        JButton sendAutomatedButton = new JButton("Send Automated Message");

        actionPanel.add(new JLabel("Enter Message:", SwingConstants.CENTER));
        actionPanel.add(messageField);
        actionPanel.add(sendTextButton);
        actionPanel.add(sendMediaButton);
        actionPanel.add(new JLabel("Interval (seconds):", SwingConstants.CENTER));
        actionPanel.add(intervalField);
        actionPanel.add(new JLabel("Number of Messages:", SwingConstants.CENTER)); // Label for message count
        actionPanel.add(messageCountField);
        actionPanel.add(sendAutomatedButton);
        rightPanel.add(actionPanel, BorderLayout.CENTER);

        // Add Panels to Main Panel
        mainPanel.add(leftPanel, BorderLayout.WEST);
        mainPanel.add(rightPanel, BorderLayout.CENTER);

        // Add Main Panel to Frame
        frame.add(mainPanel);

        // Scan QR Code Button
        JPanel topPanel = new JPanel();
        JButton scanQRButton = new JButton("Scan QR Code");
        topPanel.add(scanQRButton);
        frame.add(topPanel, BorderLayout.NORTH);

        // QR Code Button Action
        scanQRButton.addActionListener(e -> openWhatsAppWeb());

        frame.setVisible(true);

        // Load Contacts Button Action
        loadContactsButton.addActionListener(e -> loadContacts());

        // Send Text Button Action
        sendTextButton.addActionListener(e -> sendTextMessage(messageField.getText()));

        // Send Media Button Action
        sendMediaButton.addActionListener(e -> sendMediaMessage());

        // Send Automated Message Button Action
        sendAutomatedButton.addActionListener(e -> {
            try {
                int interval = Integer.parseInt(intervalField.getText());
                int count = Integer.parseInt(messageCountField.getText());
                sendAutomatedMessage(messageField.getText(), interval, count);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Please enter valid numbers for interval and message count.");
            }
        });
    }

    private void initializeWebDriver() {
        System.setProperty("webdriver.chrome.driver", "D:\\java project\\chromedriver-win64\\chromedriver.exe");
        driver = new ChromeDriver();
        contactManager = new ContactManager(driver);  // Initialize ContactManager
    }

    private void openWhatsAppWeb() {
        try {
            driver.get("https://web.whatsapp.com");
            JOptionPane.showMessageDialog(frame, "Please scan the QR Code using your phone.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Failed to open WhatsApp Web: " + e.getMessage());
        }
    }

    private void loadContacts() {
        contactPanel.removeAll();  // Clear the existing contacts

        try {
            // Wait for the main contact elements to be visible (after page loads completely)
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[@title]")));

            // Get the contact names using the ContactManager
            List<String> contactNames = contactManager.getContactNames();

            if (contactNames.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "No contacts found. Ensure that WhatsApp Web is loaded and chats are visible.");
                return;
            }

            // Loop through each contact and create a button for it
            for (String contactName : contactNames) {
                JButton contactButton = new JButton(contactName);
                contactButton.setPreferredSize(new java.awt.Dimension(200, 50));
                contactButton.addActionListener(e -> {
                    selectedContact = contactName;
                    JOptionPane.showMessageDialog(frame, "Selected Contact: " + selectedContact);
                });
                contactPanel.add(contactButton);
            }

            // Refresh the contact panel to display new buttons
            contactPanel.revalidate();
            contactPanel.repaint();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "An error occurred while loading contacts: " + e.getMessage());
        }
    }

    private void sendTextMessage(String message) {
        if (selectedContact != null && !message.isEmpty()) {
            try {
                WebElement contact = contactManager.getContactByName(selectedContact);
                contact.click();

                WebElement messageBox = driver.findElement(By.xpath("//*[@id='main']/footer/div[1]/div/span/div/div[2]/div[1]/div[2]/div[1]/p"));
                messageBox.sendKeys(message);
                messageBox.sendKeys(Keys.RETURN);

                JOptionPane.showMessageDialog(frame, "Message sent to " + selectedContact);
            } catch (NoSuchElementException e) {
                JOptionPane.showMessageDialog(frame, "Failed to send message.");
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Select a contact and enter a message.");
        }
    }

    private void sendMediaMessage() {
        if (selectedContact != null) {
            JFileChooser fileChooser = new JFileChooser();
            if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                String filePath = fileChooser.getSelectedFile().getAbsolutePath();
                try {
                    WebElement contact = contactManager.getContactByName(selectedContact);
                    contact.click();

                    WebElement attachButton = driver.findElement(By.xpath("//*[@id='main']/footer/div[1]/div/span/div/div[1]/div/button/span"));
                    attachButton.click();

                    WebElement fileInput = driver.findElement(By.xpath("//input[@type='file']"));
                    fileInput.sendKeys(filePath);

                    WebElement sendButton = driver.findElement(By.xpath("//span[@data-icon='send']"));
                    sendButton.click();

                    JOptionPane.showMessageDialog(frame, "Multimedia sent to " + selectedContact);
                } catch (NoSuchElementException e) {
                    JOptionPane.showMessageDialog(frame, "Failed to send multimedia.");
                }
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Select a contact to send media.");
        }
    }

    private void sendAutomatedMessage(String message, int intervalInSeconds, int count) {
        if (selectedContact != null && !message.isEmpty()) {
            messageCount = 0;  // Reset the counter for each automation

            // Schedule the automated message
            scheduler.scheduleAtFixedRate(() -> {
                if (messageCount < count) {
                    sendTextMessage(message);  // Reusing sendTextMessage method for automation
                    messageCount++;
                } else {
                    scheduler.shutdown(); // Stop the scheduler once the count is reached
                    JOptionPane.showMessageDialog(frame, "Automated message sending has been stopped after " + count + " messages.");
                }
            }, 0, intervalInSeconds, TimeUnit.SECONDS);
            JOptionPane.showMessageDialog(frame, "Automated messages will be sent to " + selectedContact + " every " + intervalInSeconds + " seconds.");
        } else {
            JOptionPane.showMessageDialog(frame, "Select a contact and enter a message.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new WhatsAppAutomationGUI());
    }
}
