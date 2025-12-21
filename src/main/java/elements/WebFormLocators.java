package elements;

import org.openqa.selenium.By;

public class WebFormLocators {
    public static final By TEXT_INPUT = By.id("my-text-id");
    public static final By PASSWORD = By.name("my-password");
    public static final By TEXTAREA = By.name("my-textarea");
    public static final By DISABLED_INPUT = By.name("my-disabled");
    public static final By READONLY_INPUT = By.name("my-readonly");
    public static final By RETURN_TO_INDEX = By.xpath("//a[contains(text(), 'Return')]");
    public static final By SELECT_DROPDOWN = By.name("my-select");
    public static final By DROPDOWN = By.name("my-datalist");
    public static final By FILE_INPUT = By.name("my-file");
    public static final By CHECKED_CHECKBOX = By.id("my-check-1");
    public static final By DEFAULT_CHECKBOX = By.id("my-check-2");
    public static final By CHECKED_RADIO = By.id("my-radio-1");
    public static final By DEFAULT_RADIO = By.id("my-radio-2");
    public static final By SUBMIT = By.xpath("//*[@type='submit']");
    public static final By COLOR_PICKER = By.xpath("//*[@type='color']");
    public static final By DATE_PICKER = By.xpath("//input[@type='text' and @name = 'my-date']");
    public static final By EXAMPLE_RANGE = By.name("my-range");
}
