package methods;

import org.openqa.selenium.WebDriver;
import pages.BasePage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUpload extends BasePage {

    public FileUpload(WebDriver driver)
    {
        super(driver);
    }

    public static String getPath(String fileName) throws URISyntaxException, IOException {
        //Получаем ресурс по имени файла
        URL resource = FileUpload.class.getClassLoader().getResource(fileName);
        if (resource == null) {
            throw new FileNotFoundException("Файл " + fileName + "не найден в ресурсах");
        }
        //Преобразуем URL в Path
        Path path = Paths.get(resource.toURI());
        // Читаем содержимое
        return path.toAbsolutePath().toString();
    }
}
