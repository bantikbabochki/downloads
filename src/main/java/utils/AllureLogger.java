package utils;

import io.qameta.allure.Attachment;

public class AllureLogger {
    @Attachment(value = "{title}", type = "text/plain")
    public static String attachText(String title, String message) {
        return message;
    }

    public static void logErrorToAllure(String method, Throwable error) {
        String message = "Ошибка в тесте: " + method + "\n" + error.toString();
        attachText("Ошибка в тесте", message);
    }

    public static void logInfoToAllure(String message) {
        attachText("Лог AspectJ", message);
    }
}
