package steps;

import com.google.common.io.Files;
import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;
import io.qameta.allure.Step;
import org.apache.commons.io.FileUtils;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpUriRequestBase;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import static com.codeborne.selenide.Screenshots.takeScreenShotAsFile;

public class AllureSteps {
    @Attachment(value = "Screenshot", type = "image/png")
    @Step("Capture screenshot")
    public byte[] captureScreenshot(WebDriver driver) {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }

    @Attachment(value = "Screenshot", type = "image/png")
    @Step("Capture screenshot with Selenide")
    public byte[] captureScreenshotSelenide() throws IOException {
        return Files.toByteArray(takeScreenShotAsFile());
    }

//    @Attachment(value="Screenshot", type="image/png")
//    @Step("Capture screenshot with Playwright")
//    public byte[] captureScreenshotPlaywright(Page page) {
//        return page.screenshot();
//    }

    @Step("Capture screenshot (spoiler)")//запись с сокрытием чувствительной информации
    public void captureScreenshotSpoiler(WebDriver driver) {
        Allure.addAttachment("Screenshot", new ByteArrayInputStream(((TakesScreenshot) driver)
                .getScreenshotAs(OutputType.BYTES)));
    }

//    @Step("Capture screenshot (spoiler)")//запись с сокрытием чувствительной информации
//    public void  captureScreenshotSpoiler() {
//        Allure.addAttachment("Screenshot", new ByteArrayInputStream(((TakesScreenshot) BaseSteps.getDriver())
//                .getScreenshotAs(OutputType.BYTES)));
//    }

    @Step("Capture screenshot with Selenide (extension)")
    public void captureScreenshotSelenideSpoiler() throws IOException {
        Allure.addAttachment("Screenshot", new ByteArrayInputStream(Files.toByteArray(takeScreenShotAsFile())));
    }

//    @Step("Capture screenshot with Playwright (extension)")
//    public void captureScreenshotPlaywrightSpoiler(Page page) {
//        Allure.addAttachment("Screenshot", new ByteArrayInputStream(page.screenshot()));
//    }

    /**
     * Метод для загрузки файла через HTTP-запрос
     *
     * @param link - URL файла для загрузки
     * @param destination - Файл, куда сохранить (например: new File("downloads/report.pdf"))
     * @throws IOException - если ошибка при загрузке
     */

    @Step("Download file: {destination}")
    public void download(String link, File destination) throws IOException {
        // ШАГ 1: Создание HTTP-клиента (инструмент для HTTP-запросов)
        // try-with-resources автоматически закроет клиент после использования
        try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
            // ШАГ 2: Создание GET-запроса
            HttpUriRequestBase request = new HttpGet(link);
            // ШАГ 3: Выполнение запроса и обработка ответа
            client.execute(request, (HttpClientResponseHandler<byte[]>) response -> {
                // ШАГ 4: Получение содержимого файла как поток данных
                InputStream inputStream = response.getEntity().getContent();
                // ШАГ 5А: сохранение файла на диск
                //FileUtils.copyInputStreamToFile(inputStream, destination);
                Allure.addAttachment(destination.getName(), inputStream);
                return null;
            });
        }
    }

    @Step("Download file: {destination}")
    public File downloadC(String link, File destination) throws IOException {
        try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
            HttpGet request = new HttpGet(link);

            return client.execute(request, response -> {
                byte[] fileBytes = response.getEntity()
                        .getContent()
                        .readAllBytes();

                // Сохраняем на диск
                FileUtils.writeByteArrayToFile(destination, fileBytes);

                // Прикрепляем к Allure
                Allure.addAttachment(
                        destination.getName(),
                        new ByteArrayInputStream(fileBytes)
                );

                return destination;
            });
        }
    }
}

