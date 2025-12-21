package ui;

import builder.WebFormData;
import methods.FileUpload;
import org.junit.jupiter.api.Test;
import pages.SubmittedFormPage;
import pages.WebFormPage;
import utils.PageHelper;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class WebFormBuilderTest extends BaseTest {
    @Test
    void testFillFormWithBuilder() throws URISyntaxException, IOException {
        // Подготовка данных через Builder
        WebFormData formData = WebFormData.builder()
                .textInput("Тестовый текст")
                .password("SecurePass123")
                .textArea("Многострочный текст\nВторая строка")
                .selectDropdownValue("2")
                .dataListDropdownValue("New York")
                .filePath(FileUpload.getPath("webFormFile.txt"))
                .checkedCheckbox(true)
                .defaultCheckbox(false)
                .radioValue("1")
                .colourPicker("#ff5733")
                .datePicker("2024-12-17")
                .rangeValue("8")
                .build();

        // Выполнение теста
        WebFormPage webFormPage = PageHelper.prepareWebForm(driver);
        SubmittedFormPage submittedPage = webFormPage.fillAndSubmit(formData);

        // Проверка результата
        assertTrue(submittedPage.isFormSubmittedSuccessfully(),
                "Форма не была успешно отправлена");
    }
}
