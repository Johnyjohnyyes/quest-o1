import java.math.BigDecimal;
import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

import static org.junit.jupiter.api.Assertions.assertFalse;

class Questao05Test {
    private static final BigDecimal PRICE_LIMIT = new BigDecimal("20.00");

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
    void deveListarProdutosComPrecoAbaixoDeVinteDolares() {
        driver.get("https://www.saucedemo.com/");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("user-name"))).sendKeys("standard_user");
        driver.findElement(By.id("password")).sendKeys("secret_sauce");
        driver.findElement(By.id("login-button")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='inventory-container']")));

        List<WebElement> inventoryItems = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                By.cssSelector("[data-test='inventory-item']")));
        Map<String, Product> uniqueProducts = new LinkedHashMap<>();
        for (WebElement item : inventoryItems) {
            String name = item.findElement(By.cssSelector("[data-test='inventory-item-name']")).getText();
            String displayedPrice = item.findElement(By.cssSelector("[data-test='inventory-item-price']")).getText();
            uniqueProducts.putIfAbsent(name, new Product(name, new BigDecimal(displayedPrice.replace("$", ""))));
        }

        assertFalse(uniqueProducts.isEmpty(), "O inventário deve conter produtos.");
        List<Product> affordableProducts = uniqueProducts.values().stream()
                .filter(product -> product.price().compareTo(PRICE_LIMIT) < 0)
                .toList();

        if (affordableProducts.isEmpty()) {
            System.out.println("Nenhum produto com preço inferior a $20.00.");
        } else {
            affordableProducts.forEach(product ->
                    System.out.printf("%s - $%s%n", product.name(), product.price().toPlainString()));
        }
    }

    private record Product(String name, BigDecimal price) {
    }
}