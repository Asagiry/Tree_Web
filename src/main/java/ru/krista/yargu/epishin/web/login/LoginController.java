package ru.krista.yargu.epishin.web.login;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.krista.yargu.epishin.exceptions.login.AuthDBLoginException;
import ru.krista.yargu.epishin.exceptions.login.InitDBLoginException;
import ru.krista.yargu.epishin.web.utils.Constants;
import ru.krista.yargu.epishin.web.utils.HtmlBuilder;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Контроллер входа: выдает и обрабатывает форму входа.
 */
@Path(Constants.LOGIN_BASE_PATH)
public class LoginController {

    private final Logger logger = LogManager.getLogger();
    private final LoginService loginService;

    public LoginController() throws InitDBLoginException {
        loginService = new LoginService();
    }


    @GET
    @Produces("text/html")
    public String getForm() {
        return HtmlBuilder.buildStart("Вход")+
            "    <h1>Вход</h1>" +
            "    <form method=\"post\" action=\""+Constants.LOGIN_BASE_PATH+"\">" +
            "      <label for=\"uname\"><b>Username</b></label>\n" +
            "      <input type=\"text\" placeholder=\"Enter Username\" name=\"username\" required>\n" +
            "      <label for=\"psw\"><b>Password</b></label>\n" +
            "      <input type=\"password\" placeholder=\"Enter Password\" name=\"password\" required>\n" +
            "      <button type=\"submit\">Login</button>" +
            "    </form>" +
            HtmlBuilder.buildEnd();
    }

    @POST
    @Produces("text/html")
    public Response login(@FormParam("username") String username, @FormParam("password") String password) {
        try {
            if (loginService.login(username, password)) {
                return Response.seeOther(new URI(Constants.LOGIN_SUCCESS_PATH)).build();
            } else {
                return Response.seeOther(new URI(Constants.LOGIN_FAILURE_PATH)).build();
            }
        } catch (URISyntaxException e) {
            throw new IllegalStateException(Constants.URI_SYNTAX_ERROR_MESSAGE);
        } catch (AuthDBLoginException e) {
            logger.error(e.getMessage(),e.getCause());
            try {
                return Response.seeOther(new URI(Constants.LOGIN_BASE_PATH)).build();
            } catch (URISyntaxException ex) {
                throw new IllegalStateException(Constants.URI_SYNTAX_ERROR_MESSAGE);
            }
        }

    }

    @GET
    @Path("/success")
    @Produces("text/html")
    public String getSuccessPage() {
        return
                HtmlBuilder.buildStart("Успешный вход")+
                "    <h1>Успешный вход</h1>" +
                "    <a href=\""+Constants.LOGIN_BASE_PATH+"\">Назад</a>" +
                HtmlBuilder.buildEnd();
    }

    @GET
    @Path("/failure")
    @Produces("text/html")
    public String getFailurePage() {
        return
               HtmlBuilder.buildStart("Ошибка входа")+
                "    <h1>Ошибка входа</h1>" +
                "    <a href=\""+Constants.LOGIN_BASE_PATH+">Назад</a>" +
                HtmlBuilder.buildEnd();
    }
}
