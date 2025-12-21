package utils;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.apache.commons.io.FileUtils;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

public class FileDownloader {
    // Базовая папка для всех загрузок
    private static final String DOWNLOAD_DIR = "test-output/downloads";

    /**
     * Скачать файл по прямой ссылке
     *
     * @param link URL файла
     * @param fileName Имя файла для сохранения (например "report.pdf")
     * @return Скачанный файл
     * @throws IOException если ошибка при загрузке
     */
    @Step("Скачивание файла: {fileName}")
    public static File download(String link, String fileName) throws IOException {
        // 1. Определяем путь к папке загрузок
        String projectDir = System.getProperty("user.dir");
        File downloadDir = new File(projectDir, DOWNLOAD_DIR);

        // 2. Создаем папку, если её нет
        if (!downloadDir.exists()) {
            boolean created = downloadDir.mkdirs();
            if (created) {
                System.out.println("✅ Создана папка: " + downloadDir.getAbsolutePath());
            }
        }

        // 3. Определяем полный путь к файлу
        File destination = new File(downloadDir, fileName);

        // 4. Удаляем старый файл, если существует
        if (destination.exists()) {
            destination.delete();
            System.out.println("⚠️ Удален старый файл: " + fileName);
        }

        // 5. Скачиваем файл через HTTP
        System.out.println("📥 Скачивание: " + link);
        try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
            HttpGet request = new HttpGet(link);

            client.execute(request, response -> {
                // Проверяем статус ответа
                int statusCode = response.getCode();
                if (statusCode != 200) {
                    throw new IOException("Ошибка загрузки. HTTP код: " + statusCode);
                }

                // Читаем содержимое файла
                byte[] fileBytes = response.getEntity().getContent().readAllBytes();

                // Сохраняем на диск
                FileUtils.writeByteArrayToFile(destination, fileBytes);

                // Прикрепляем к отчету Allure
                Allure.addAttachment(fileName, new ByteArrayInputStream(fileBytes));

                return null;
            });
        }

        // 6. Проверяем результат
        if (!destination.exists() || destination.length() == 0) {
            throw new IOException("Файл не был скачан или пустой!");
        }

        System.out.println("✅ Файл сохранен: " + destination.getAbsolutePath());
        System.out.println("   Размер: " + destination.length() + " байт");

        return destination;
    }

    /**
     * Скачать файл по прямой ссылке (упрощенная версия)
     * Имя файла берется из URL
     */
    @Step("Скачивание файла из URL")
    public static File download(String link) throws IOException {
        // Извлекаем имя файла из URL
        String fileName = link.substring(link.lastIndexOf('/') + 1);
        return download(link, fileName);
    }

    /**
     * Получить путь к папке загрузок
     */
    public static String getDownloadPath() {
        String projectDir = System.getProperty("user.dir");
        return new File(projectDir, DOWNLOAD_DIR).getAbsolutePath();
    }

    /**
     * Очистить папку загрузок
     */
    @Step("Очистка папки загрузок")
    public static void cleanDownloads() throws IOException {
        String projectDir = System.getProperty("user.dir");
        File downloadDir = new File(projectDir, DOWNLOAD_DIR);

        if (downloadDir.exists()) {
            FileUtils.cleanDirectory(downloadDir);
            System.out.println("✅ Папка загрузок очищена");
        }
    }

    /**
     * Проверить, существует ли файл в папке загрузок
     */
    public static boolean isFileDownloaded(String fileName) {
        String projectDir = System.getProperty("user.dir");
        File file = new File(projectDir, DOWNLOAD_DIR + "/" + fileName);
        return file.exists() && file.length() > 0;
    }
}

