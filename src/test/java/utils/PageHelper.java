package utils;

import org.openqa.selenium.WebDriver;
import pages.HomePage;
import pages.WebFormPage;

public class PageHelper {
    public static WebFormPage prepareWebForm(WebDriver driver) {
        HomePage homePage = new HomePage(driver).openBaseUrl();
        WebFormPage webFormPage = homePage.openWebFormPage();
        webFormPage.checkIsWebFormPage();
        return webFormPage;
    }
}
