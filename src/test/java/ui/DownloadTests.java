package ui;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import steps.AllureSteps;
import utils.FileDownloader;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class DownloadTests extends BaseTest {

    @Test
    void testDownloadHttpClient() throws IOException {
        // Открываем страницу
        driver.get("https://bonigarcia.dev/selenium-webdriver-java/download.html");

        // Находим ссылку на файл
        WebElement pngLink = driver.findElement(By.xpath("(//a)[2]"));
        String fileUrl = pngLink.getAttribute("href");

        // Скачиваем через наш метод (БЕЗ клика в браузере!)
        File downloadedFile = FileDownloader.download(fileUrl, "WebDriverManager logo");

//        // Проверяем содержимое
//        String content = org.apache.commons.io.FileUtils.readFileToString(
//                downloadedFile, "UTF-8"
//        );
//        assertTrue(content.contains("WebDriverManager logo"), "Неверное содержимое файла");
    }

    @AfterEach
    void cleanup() throws IOException {
        // Очищаем папку после каждого теста (опционально)
        // FileDownloader.cleanDownloads();
    }
}

