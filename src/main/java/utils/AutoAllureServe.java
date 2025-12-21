package utils;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class AutoAllureServe {
    private static final int PORT = 8080;
    private static final String REPORT_DIR = "build/reports/allure-report/allureReport";

    public static void main(String[] args) throws IOException, InterruptedException {
        Path reportRoot = Paths.get(REPORT_DIR);
        if (!Files.exists(reportRoot) || !Files.isDirectory(reportRoot)) {
            System.err.println("❌ Отчёт не найден: " + reportRoot.toAbsolutePath());
            System.err.println("Запустите сначала: ./gradlew allureReport");
            return;
        }

        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext("/", new AllureHandler(reportRoot));
        server.setExecutor(null);
        server.start();

        String url = "http://localhost:" + PORT;
        System.out.println("🚀 Allure отчёт доступен: " + url);
        System.out.println("⏳ Автоматическое завершение через 30 секунд...");

        // === Автоматически открываем браузер ===
        try {
            java.awt.Desktop.getDesktop().browse(java.net.URI.create(url));
            System.out.println("✅ Браузер открыт автоматически.");
        } catch (Exception e) {
            System.err.println("⚠️ Не удалось открыть браузер автоматически. Откройте вручную: " + url);
        }

        Thread.sleep(30_000);

        server.stop(1);
        System.out.println("⏹️ Сервер остановлен.");
    }

    static class AllureHandler implements HttpHandler {
        private final Path reportRoot;

        AllureHandler(Path reportRoot) {
            this.reportRoot = reportRoot;
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String requestPath = exchange.getRequestURI().getPath();
            if ("/".equals(requestPath)) {
                requestPath = "/index.html";
            }

            Path filePath = reportRoot.resolve(requestPath.substring(1)).normalize();
            if (!filePath.startsWith(reportRoot) || !Files.exists(filePath)) {
                send404(exchange);
                return;
            }

            byte[] bytes = Files.readAllBytes(filePath);
            exchange.getResponseHeaders().set("Content-Type", getContentType(filePath.toString()));
            exchange.sendResponseHeaders(200, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        }

        private void send404(HttpExchange exchange) throws IOException {
            String response = "404 Not Found";
            exchange.sendResponseHeaders(404, response.length());
            exchange.getResponseBody().write(response.getBytes());
            exchange.close();
        }

        private String getContentType(String path) {
            if (path.endsWith(".html")) return "text/html";
            if (path.endsWith(".js")) return "application/javascript";
            if (path.endsWith(".css")) return "text/css";
            if (path.endsWith(".png")) return "image/png";
            if (path.endsWith(".jpg")) return "image/jpeg";
            if (path.endsWith(".json")) return "application/json";
            return "application/octet-stream";
        }
    }
}
