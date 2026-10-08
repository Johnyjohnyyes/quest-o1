import java.time.Duration;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class Questao03Test {
    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new", "--no-sandbox", "--disable-dev-shm-usage", "--window-size=1920,1080");
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    void deveAdicionarTresProdutosERemoverUmDoCarrinho() {
        driver.get("https://www.saucedemo.com/");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("user-name"))).sendKeys("standard_user");
        driver.findElement(By.id("password")).sendKeys("secret_sauce");
        driver.findElement(By.id("login-button")).click();
        wait.until(ExpectedConditions.urlContains("inventory.html"));

        List<String> productIds = List.of(
                "add-to-cart-sauce-labs-backpack",
                "add-to-cart-sauce-labs-bike-light",
                "add-to-cart-sauce-labs-bolt-t-shirt");
        for (String productId : productIds) {
            wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-test='" + productId + "']"))).click();
        }

        assertEquals("3", wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("shopping_cart_badge"))).getText());
        driver.findElement(By.className("shopping_cart_link")).click();
        wait.until(ExpectedConditions.urlContains("cart.html"));

        By cartItems = By.cssSelector(".cart_item");
        wait.until(ExpectedConditions.numberOfElementsToBe(cartItems, 3));
        assertEquals(3, driver.findElements(cartItems).size());

        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-test='remove-sauce-labs-backpack']"))).click();
        wait.until(ExpectedConditions.numberOfElementsToBe(cartItems, 2));

        List<String> remainingNames = driver.findElements(By.cssSelector(".cart_item .inventory_item_name"))
                .stream()
                .map(WebElement::getText)
                .toList();
        assertEquals(2, remainingNames.size());
        assertFalse(remainingNames.contains("Sauce Labs Backpack"));
        assertEquals("2", wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("shopping_cart_badge"))).getText());
    }
}