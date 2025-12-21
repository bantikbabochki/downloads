package ui;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.FileDownloadMode;
import com.codeborne.selenide.SelenideElement;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import java.io.File;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static utils.PdfUtils.savePdf;


public class SelenideDownloadTests {
    @Test
    void downloadButtonGetTest() {
        // Указываем режим скачивания файлов
        Configuration.fileDownload = FileDownloadMode.HTTPGET;
        // Указываем директорию для сохранения скачанных файлов
        Configuration.downloadsFolder = "src/main/resources";
        // Открываем страницу с кнопкой для скачивания
        open("https://bonigarcia.dev/selenium-webdriver-java/download.html");
        // Находим элемент кнопки для скачивания
        SelenideElement downloadButton = $(By.xpath("//a[text() = 'WebDriverManager logo']"));
        // Кликаем по кнопке для скачивания и сохраняем файл
        File downloadFile = downloadButton.download();
        // Проверяем, что файл был скачан успешно
        assertThat(downloadFile).exists();
    }

    @Test
    void downloadButtonFolderTest() {
        // Указываем режим скачивания файлов
        Configuration.fileDownload = FileDownloadMode.FOLDER;
        // Указываем директорию для сохранения скачанных файлов
        Configuration.downloadsFolder = "src/main/resources";
        // Открываем страницу с кнопкой для скачивания
        open("https://bonigarcia.dev/selenium-webdriver-java/download.html");
        // Находим элемент кнопки для скачивания
        SelenideElement downloadButton = $(By.xpath("//a[text() = 'WebDriverManager logo']"));
        // Кликаем по кнопке для скачивания и сохраняем файл
        File downloadedFile = downloadButton.download();
        // Проверяем, что файл был скачан успешно
        assertThat(downloadedFile).exists();
    }

    @Test
    void downloadCdpTest() {
        // Указываем режим скачивания файлов
        Configuration.fileDownload = FileDownloadMode.CDP;
        // Указываем директорию для сохранения скачанных файлов
        Configuration.downloadsFolder = "src/main/resources";
        // Открываем страницу с кнопкой для скачивания
        open("https://bonigarcia.dev/selenium-webdriver-java/download.html");
        // Находим элемент кнопки для скачивания
        SelenideElement downloadButton = $(By.xpath("//a[text() = 'WebDriverManager logo']"));
        // Кликаем по кнопке для скачивания и сохраняем файл
        File downloadedFile = downloadButton.download();
        // Проверяем, что файл был скачан успешно
        assertThat(downloadedFile).exists();
    }

    @Test
    void testDownloadHttpClient() {
        String endpoint = "https://alfabank.servicecdn.ru/site-upload/67/dd/356/zayavlenie-IZK.pdf";
        String fileName = "downloaded.pdf";

        Response response =
                given().
                        when().
                        get(endpoint).
                        then().
                        contentType("application/pdf").
                        statusCode(200).
                        extract().response();
        savePdf(response, fileName);
        // Проверяем, что файл был скачан успешно
        File downloadedFile = new File(fileName);
        assertThat(downloadedFile).exists();
    }
}
