package pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class SubmittedFormPage extends BasePage{
    private static final By SUCCESS_MESSAGE = By.className("lead");

    public SubmittedFormPage(WebDriver driver) {
        super(driver);
    }

    @Step("Получение сообщения об успехе")
    public String getSuccessMessage() {
        return getElement(SUCCESS_MESSAGE).getText();
    }

    @Step("Проверка успешной отправки формы")
    public boolean isFormSubmittedSuccessfully() {
        return getSuccessMessage().contains("Received!");
    }
}
