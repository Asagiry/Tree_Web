package ru.krista.yargu.epishin.web;

import io.undertow.Undertow;
import org.jboss.resteasy.plugins.server.undertow.UndertowJaxrsServer;
import ru.krista.yargu.epishin.tree.Node;
import ru.krista.yargu.epishin.web.utils.Constants;

import java.io.IOException;

/**
 * Консольное приложение, запускающее web-сервер.
 */
public class WebServer {

    private static RestApplication restApplication;
    private static UndertowJaxrsServer server;


    public static void main(String[] argv) {
        start();
    }

    /**
     * Запускает web-сервер. По окончании работы требуется ручная остановка процесса.
     */
    private static void start() {
        restApplication = new RestApplication();

        server = new UndertowJaxrsServer()
                .start(Undertow.builder()
                        .addHttpListener(Constants.SERVER_PORT, Constants.SERVER_HOST));

        server.deploy(restApplication);

        System.out.println("Сервер запущен: "+Constants.SERVER_URL+Constants.TREE_BASE_PATH);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Остановка сервера...");
            Node tree = restApplication.getTree();
            try {
                tree.writeJsonFileTree(Constants.TREE_FILE_PATH);
                server.stop();
                System.out.println("Сервер остановлен успешно, дерево сохранено");
            } catch (IOException e) {
                System.err.println("Не удалось сохранить дерево: " + e.getMessage());
            }
        }));
    }
}
