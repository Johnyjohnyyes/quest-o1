import java.math.BigDecimal;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Comparator;
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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class Questao04Test {
    private static final By PRODUCT_NAMES = By.cssSelector("#tbodyid .card-title a");
    private static final By PRODUCT_CARDS = By.cssSelector("#tbodyid .card");

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new", "--no-sandbox", "--disable-dev-shm-usage", "--window-size=1920,1080");
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    void deveExtrairELocalizarOLaptopMaisCaroExibido() {
        driver.get("https://www.demoblaze.com/");
        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(PRODUCT_CARDS, 0));
        List<String> initialNames = productNames();

        driver.findElement(By.linkText("Laptops")).click();
        wait.until(currentDriver -> {
            List<String> names = currentDriver.findElements(PRODUCT_NAMES).stream()
                    .map(WebElement::getText)
                    .toList();
            return !names.isEmpty() && !names.equals(initialNames);
        });
        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(PRODUCT_CARDS, 0));

        List<Product> laptops = new ArrayList<>();
        for (WebElement card : driver.findElements(PRODUCT_CARDS)) {
            String name = card.findElement(By.cssSelector(".card-title a")).getText();
            String displayedPrice = card.findElement(By.cssSelector(".card-block h5")).getText();
            laptops.add(new Product(name, parsePrice(displayedPrice)));
        }

        assertFalse(laptops.isEmpty(), "A categoria Laptops deve exibir produtos.");
        Product mostExpensive = laptops.stream().max(Comparator.comparing(Product::price)).orElseThrow();
        System.out.printf("Laptop mais caro: %s - $%s%n", mostExpensive.name(), mostExpensive.price().toPlainString());
        assertTrue(mostExpensive.price().compareTo(BigDecimal.ZERO) > 0);
    }

    private List<String> productNames() {
        return driver.findElements(PRODUCT_NAMES).stream()
                .map(WebElement::getText)
                .toList();
    }

    private BigDecimal parsePrice(String displayedPrice) {
        return new BigDecimal(displayedPrice.replaceAll("[^0-9.]", ""));
    }

    private record Product(String name, BigDecimal price) {
    }
}