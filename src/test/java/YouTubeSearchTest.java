import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import io.github.bonigarcia.wdm.WebDriverManager;

public class YouTubeSearchTest {
    private WebDriver driver;

    @BeforeEach
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
    }

    @Test
    public void testSearchCognizantOnYouTube() {
        driver.get("https://www.youtube.com");
        // Wait for popup and close if present
        try {
            Thread.sleep(2000);
            // Try to find and close the popup (e.g., sign-in or cookie consent)
            if (!driver.findElements(By.xpath("//ytd-button-renderer[@id='dismiss-button']")).isEmpty()) {
                driver.findElement(By.xpath("//ytd-button-renderer[@id='dismiss-button']")).click();
            } else if (!driver.findElements(By.xpath("//button[contains(text(),'No thanks') or contains(text(),'Reject all')]")).isEmpty()) {
                driver.findElement(By.xpath("//button[contains(text(),'No thanks') or contains(text(),'Reject all')]")).click();
            }
        } catch (Exception e) {
            // Ignore if popup not found
        }

        // Take screenshot after closing popup
        takeScreenshot("screenshot_before_search.png");

        WebElement searchBox = driver.findElement(By.name("search_query"));
        searchBox.sendKeys("Cognizant");
        searchBox.submit();
        // Wait for results to load
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        // Take screenshot after search
        takeScreenshot("screenshot_after_search.png");
        String pageSource = driver.getPageSource();
        assertTrue(pageSource.contains("Cognizant"), "Search results should contain 'Cognizant'");
    }

    private void takeScreenshot(String fileName) {
        try {
            org.openqa.selenium.TakesScreenshot ts = (org.openqa.selenium.TakesScreenshot) driver;
            java.io.File src = ts.getScreenshotAs(org.openqa.selenium.OutputType.FILE);
            java.nio.file.Files.copy(src.toPath(), java.nio.file.Paths.get(fileName), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            System.out.println("Screenshot failed: " + e.getMessage());
        }
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
