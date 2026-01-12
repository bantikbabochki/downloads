package ui;

import configs.TestPropertiesConfig;
import io.qameta.allure.Allure;
import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import patterns.WebDriverFactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.Map;

public class BaseTest {
    protected WebDriver driver;
    protected WebDriverWait wait;
    private final TestPropertiesConfig config = ConfigFactory.create(TestPropertiesConfig.class);
    private final WebDriverFactory driverFactory = new WebDriverFactory(config);

    @BeforeEach
    void setup() {
        initDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(2));
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    private void initDriver() {
        String remoteUrl = System.getenv("SELENIUM_REMOTE_URL");
        if (remoteUrl != null || !remoteUrl.isEmpty()) {
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--headless");  // Add headless mode
            options.addArguments("--disable-gpu"); // Switch off GPU, because we don't need it in headless mode
            options.addArguments("--no-sandbox"); // Switch off sandbox to prevent access rights issues
            options.addArguments("--disable-dev-shm-usage"); // Use /tmp instead of /dev/shm
            options.setCapability("goog:loggingPrefs", Map.of("browser", "ALL"));
            try {
                driver = new RemoteWebDriver(new URL(remoteUrl), options);
            } catch (MalformedURLException e) {
                throw new RuntimeException("Malformed URL for Selenium Remote WebDriver", e);
            }
        } else {
            Allure.addAttachment("Local run", "No remote driver");
            driver = driverFactory.createWebDriver();
        }
    }
}
