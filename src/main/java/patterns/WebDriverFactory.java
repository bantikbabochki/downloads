package patterns;

import configs.TestPropertiesConfig;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URI;

import static com.codeborne.selenide.Browsers.CHROME;
import static com.codeborne.selenide.Browsers.EDGE;

public class WebDriverFactory {
    private static TestPropertiesConfig config;

    public WebDriverFactory(TestPropertiesConfig config) {
        WebDriverFactory.config = config;
    }

    public WebDriver createWebDriver() {
        String browser = config.browser();
        return switch (browser.toLowerCase()) {
            case CHROME -> createChromeDriver();
            case EDGE -> new EdgeDriver();
            default -> throw new IllegalArgumentException("Unsupported browser: " + browser);
        };
    }

    private static WebDriver createChromeDriver() {
        String remoteUrl = config.remoteUrl();

        ChromeOptions options = new ChromeOptions();
        if (remoteUrl != null && !remoteUrl.isEmpty()) {
            options.addArguments("--headless", "--no-sandbox",
                    "--disable-gpu", "--disable-dev-shm-usage");
            try {
                return new RemoteWebDriver(URI.create(remoteUrl).toURL(), options);
            } catch (MalformedURLException e) {
                throw new RuntimeException("Invalid remote URL: " + remoteUrl, e);
            }
        }
        return new ChromeDriver(options);
    }
}
