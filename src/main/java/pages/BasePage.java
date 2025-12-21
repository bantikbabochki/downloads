package pages;

import Enums.LocatorType;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class BasePage {
    protected WebDriver driver;
    protected WebDriverWait wait;

    //Locators
    @FindBy(className = "display-6")
    private WebElement title;

    //Конструктор класса
    public BasePage(WebDriver driver) {
        //Сохраняет переданный драйвер в поле класса, чтобы использовать в других методах
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }

    @Step("Getting current URL")
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    @Step("Get subpage title")
    public String getTitle() {
        return title.getText();
    }

    // ============================================
    // Основной метод работы с элементами
    // ============================================

    @Step("Получение элемента")
    public WebElement getElement(By locator) {
        try {
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            assert element != null;
            assertTrue(element.isDisplayed(), "Элемент найден, но не отображается");
            return element;
        } catch (Exception e) {
            throw new AssertionError("Элемент не найден или не отображается: " + locator, e);
        }
    }

    @Step("Получение элемента по динамическому локатору")
    public WebElement getElement(LocatorType type, String value) {
        return getElement(createLocator(type, value));
    }

    @Step("Проверка видимости элемента")
    public boolean isElementVisible(By locator) {
        try {
            return getElement(locator).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    @Step("Проверка уникальности элемента")
    public void checkElementUnique(By locator) {
        List<WebElement> elements = driver.findElements(locator);

        if (elements.size() != 1) {
            String message = elements.isEmpty()
                    ? "Элемент не найден: тип=" + locator
                    : "Найдено несколько элементов (" + elements.size() + "): " + locator;
            throw new AssertionError(message);
        }
    }

    private By createLocator(LocatorType type, String value) {
        return switch (type) {
            case ID -> By.id(value);//то же что и case ID: locator=By.id(value); break;
            case NAME -> By.name(value);
            case CLASS_NAME -> By.className(value);
            case XPATH -> By.xpath(value);
            case CSS_SELECTOR -> By.cssSelector(value);
            case TAG_NAME -> By.tagName(value);
            case LINK_TEXT -> By.linkText(value);
            case PARTIAL_LINK_TEXT -> By.partialLinkText(value);
        };
    }
}
