package ru.krista.yargu.epishin.web;

import io.undertow.Undertow;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;
import org.jboss.resteasy.plugins.server.undertow.UndertowJaxrsServer;
import ru.krista.yargu.epishin.web.utils.Constants;

/**
 * Консольное приложение, запускающее web-сервер.
 */
public class WebServer {

    private static final Logger logger = LogManager.getLogger();

    public static void main(String[] argv) {
        start();
    }

    /**
     * Запускает web-сервер. По окончании работы требуется ручная остановка процесса.
     */
    private static void start() {
        Configurator.setLevel("ru.krista.yargu.epishin", Level.TRACE);

        RestApplication restApplication = new RestApplication();

        UndertowJaxrsServer server = new UndertowJaxrsServer()
                .start(Undertow.builder()
                        .addHttpListener(Constants.SERVER_PORT, Constants.SERVER_HOST));

        server.deploy(restApplication);

        logger.info("Сервер запущен: "+Constants.SERVER_URL+Constants.TREE_BASE_PATH);
    }
}
