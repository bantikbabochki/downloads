package pages;

import builder.WebFormData;
import elements.WebFormLocators;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class WebFormPage extends BasePage {
    private static final String WEB_FORM_URL = "web-form.html";

    public WebFormPage(WebDriver driver)
    {
        super(driver);
    }

    @Step("Get subpage url")
    public String getUrl() {
        return WEB_FORM_URL;
    }

    @Step("Check that page is Web form")
    public void checkIsWebFormPage() {
        String expectedUrl = HomePage.BASE_URL.trim()+WEB_FORM_URL;
        assertEquals(expectedUrl, getCurrentUrl().trim(), "URL не совпадает");
        assertEquals("Web form", getTitle(), "Заголовок страницы не совпадает");
    }

    // ============================================
    // Методы заполнения отдельных полей
    // ============================================

    @Step("Ввод текста в поле Text Input: {text}")
    public void fillTextInput(String text) {
        if (text != null) {
            WebElement element = getElement(WebFormLocators.TEXT_INPUT);
            element.clear();
            element.sendKeys(text);
        }
    }

    @Step("Ввод пароля: ***")
    public void fillPassword(String password) {
        if (password != null) {
            WebElement element = getElement(WebFormLocators.PASSWORD);
            element.clear();
            element.sendKeys(password);
        }
    }

    @Step("Ввод текста в Textarea: {text}")
    public void fillTextarea(String text) {
        if (text != null) {
            WebElement element = getElement(WebFormLocators.TEXTAREA);
            element.clear();
            element.sendKeys(text);
        }
    }

    @Step("Выбор значения в Dropdown: {value}")
    public void selectDropdown(String value) {
        if (value != null) {
            WebElement dropdownElement = getElement(WebFormLocators.SELECT_DROPDOWN);
            Select dropdown = new Select(dropdownElement);
            dropdown.selectByValue(value);
        }
    }

    @Step("Ввод значения в Datalist: {value}")
    public void fillDatalist(String value) {
        if (value != null) {
            WebElement element = getElement(WebFormLocators.DROPDOWN);
            element.clear();
            element.sendKeys(value);
        }
    }

    @Step("Загрузка файла: {filePath}")
    public void uploadFile(String filePath) {
        if (filePath != null) {
            WebElement element = getElement(WebFormLocators.FILE_INPUT);
            element.sendKeys(filePath);
        }
    }

    @Step("Установка Checked Checkbox: {checked}")
    public void setCheckedCheckbox(boolean checked) {
        WebElement checkbox = getElement(WebFormLocators.CHECKED_CHECKBOX);
        if (checkbox.isSelected() != checked) {
            checkbox.click();
        }
    }

    @Step("Установка Default Checkbox: {checked}")
    public void setDefaultCheckbox(boolean checked) {
        WebElement checkbox = getElement(WebFormLocators.DEFAULT_CHECKBOX);
        if (checkbox.isSelected() != checked) {
            checkbox.click();
        }
    }

    @Step("Выбор Radio кнопки: {value}")
    public void selectRadio(String value) {
        if (value != null) {
            WebElement radio = driver.findElement(
                    org.openqa.selenium.By.id("my-radio-" + value)
            );
            if (!radio.isSelected()) {
                radio.click();
            }
        }
    }

    @Step("Выбор цвета: {color}")
    public void selectColor(String color) {
        if (color != null) {
            WebElement element = getElement(WebFormLocators.COLOR_PICKER);
            element.sendKeys(color);
        }
    }

    @Step("Выбор даты: {date}")
    public void selectDate(String date) {
        if (date != null) {
            WebElement element = getElement(WebFormLocators.DATE_PICKER);
            element.clear();
            element.sendKeys(date);
        }
    }

    @Step("Установка значения Range: {value}")
    public void setRange(String value) {
        if (value != null) {
            WebElement element = getElement(WebFormLocators.EXAMPLE_RANGE);
            element.sendKeys(value);
        }
    }

    // ============================================
    // Заполнение формы через Builder
    // ============================================

    @Step("Заполнение формы данными из Builder")
    public WebFormPage fillForm(WebFormData data) {
        fillTextInput(data.getTextInput());
        fillPassword(data.getPassword());
        fillTextarea(data.getTextArea());
        selectDropdown(data.getSelectDropdownValue());
        fillDatalist(data.getDataListDropdownValue());
        uploadFile(data.getFilePath());

        if (data.getCheckedCheckbox() != null) {
            setCheckedCheckbox(data.getCheckedCheckbox());
        }
        if (data.getDefaultCheckbox() != null) {
            setDefaultCheckbox(data.getDefaultCheckbox());
        }

        selectRadio(data.getRadioValue());
        selectColor(data.getColourPicker());
        selectDate(data.getDatePicker());
        setRange(data.getRangeValue());

        return this;
    }

    @Step("Отправка формы")
    public SubmittedFormPage submitForm() {
        WebElement submitButton = getElement(WebFormLocators.SUBMIT);
        submitButton.click();
        return new SubmittedFormPage(driver);
    }

    @Step("Заполнение и отправка формы")
    public SubmittedFormPage fillAndSubmit(WebFormData data) {
        return fillForm(data).submitForm();
    }
}
