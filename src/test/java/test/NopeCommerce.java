package test;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;

public class NopeCommerce{

    private static final String FILE_PATH = "C:\\Users\\Menna\\IdeaProjects\\Web\\data2.csv";
    private static final String URL = "https://practicesoftwaretesting.com/";

    public static void main(String[] args) throws InterruptedException {
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();

        try (CSVReader csvReader = new CSVReader(new FileReader(FILE_PATH))) {
            String[] nextLine;

            // Create a wait object for dynamic waits
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            while ((nextLine = csvReader.readNext()) != null) {
                String address = nextLine[0]; // Address from CSV
                String city = nextLine[1];     // City from CSV
                String state = nextLine[2];    // State from CSV
                String country = nextLine[3];  // Country from CSV
                String postcode = nextLine[4]; // Postal code from CSV
                // Maximize the browser window
                driver.manage().window().maximize();
                // Navigate to the nopCommerce website
                driver.get(URL);

                // Select a product
                selectProduct(driver, wait);

                // Add product to cart
                addToCart(driver, wait);

                // Proceed to checkout
                proceedToCheckout(driver, wait);

                // Fill in shipping information
                fillShippingInfo(driver, address, city, state, country, postcode);

                // Complete the order
                completeOrder(driver, wait);

                // Optionally navigate back to the homepage for the next iteration
              //  driver.get(URL);
            }
        } catch (IOException | CsvValidationException e) {
            System.err.println("Error reading the CSV file: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
        } finally {
            // Close the driver
            Thread.sleep(3000);
            driver.quit();
        }
    }

    private static void selectProduct(WebDriver driver, WebDriverWait wait) {
        WebElement productLink = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.card-body")));
        System.out.println("Selected product: " + productLink.getText());
        productLink.click();
    }

    private static void addToCart(WebDriver driver, WebDriverWait wait) {
        WebElement addToCartButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("btn-add-to-cart")));
        addToCartButton.click();

        // Click on the cart
        WebElement cartLink = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("span#lblCartCount")));
        cartLink.click();
    }

    private static void proceedToCheckout(WebDriver driver, WebDriverWait wait) {
        // Click on the proceed button
        WebElement checkoutButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("button.btn-success")));
        checkoutButton.click();

        // Login
        driver.findElement(By.id("email")).sendKeys("gwdeecken@racfq.com");
        driver.findElement(By.id("password")).sendKeys("12345mM@");
        driver.findElement(By.className("btnSubmit")).click();

        // Click on the checkout button
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[@data-test='proceed-2']"))).click();
    }

    private static void fillShippingInfo(WebDriver driver, String address, String city, String state, String country, String postcode) {
        driver.findElement(By.id("address")).sendKeys(address);
        driver.findElement(By.id("city")).sendKeys(city);
        driver.findElement(By.id("state")).sendKeys(state);
        driver.findElement(By.id("country")).sendKeys(country);
        driver.findElement(By.id("postcode")).sendKeys(postcode);
    }

    private static void completeOrder(WebDriver driver, WebDriverWait wait) {
        // Click on proceed button
        WebElement completeOrderButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("button[data-test=\"proceed-3\"]")));
        completeOrderButton.click();

        // Choose payment method
        WebElement paymentMethodSelect = driver.findElement(By.xpath("//select[@data-test='payment-method']"));
        Select select = new Select(paymentMethodSelect);
        select.selectByValue("cash-on-delivery");

        driver.findElement(By.cssSelector("button[data-test='finish']")).click();
    }
}
