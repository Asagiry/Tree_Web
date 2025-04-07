package ru.krista.yargu.epishin.web.list;

import ru.krista.yargu.epishin.web.utils.Constants;
import ru.krista.yargu.epishin.web.utils.HtmlBuilder;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

/**
 * Контроллер отвечающий за представление списка.
 */
@Path("/list")
public class ListPresentationController {
    private final List<String> list;

    /**
     * Запоминает список, с которым будет работать.
     * @param list список, с которым будет работать контроллер.
     */
    public ListPresentationController(List<String> list) {
        this.list = list;
    }

    /**
     * Пример вывода простого текста.
     */
    @GET
    @Path("example")
    @Produces("text/plain")
    public String getSimpleText() {
        return "hello world";
    }

    /**
     * Выводит HTML-страницу со списком, ссылками на страницы редактирования и копкой добавления записи.
     * @return HTML-страница со списком.
     */
    @GET
    @Path("/")
    @Produces("text/html")
    public String getList() {
        StringBuilder result = new StringBuilder()
                .append(HtmlBuilder.buildStart("Вывод списка"))
                .append("    <h1>Список</h1>")
                .append("    <ul>");

        for (int i = 0; i < list.size(); i++) {
            result.append("<li>")
                    .append(list.get(i))
                    .append(" <a href=\"edit/")
                    .append(i)
                    .append("\">Редактировать</a> </li>");
        }

        result.append("    </ul>")
                .append("      <br/>")
                .append("      <form method=\"post\" action=\"add_random_item\">")
                .append("        <input type=\"submit\" value=\"Add random item\"/>")
                .append("      </form>")
                .append(HtmlBuilder.buildEnd());

        return result.toString();
    }

    /**
     * Пример обработки POST запроса.
     * Добавляет одну случайную запись в список и перенаправляет пользователя на основную страницу со списком.
     * @return перенаправление на основную страницу со списком.
     */
    @POST
    @Path("add_random_item")
    @Produces("text/html")
    public Response addRandomItem() {
        list.add("zzz");
        try {
            return Response.seeOther(new URI("/")).build();
        } catch (URISyntaxException e) {
            throw new IllegalStateException(Constants.URI_SYNTAX_ERROR_MESSAGE);
        }
    }

    /**
     * Выводит страничку для редактирования одного элемента.
     * @param itemId индекс элемента списка.
     * @return страничка для редактирования одного элемента.
     */
    @GET
    @Path("/edit/{id}")
    @Produces("text/html")
    public String getEditPage(@PathParam("id") int itemId) {
        String listItem = list.get(itemId);

        return HtmlBuilder.buildStart("Редактирование элемента списка") +
                "    <h1>Редактирование элемента списка</h1>" +
                "    <form method=\"post\" action=\"/edit/" + itemId + "\">" +
                "      <p>Значение</p>" +
                "      <input type=\"text\" name=\"value\" value=\"" + listItem + "\"/>" +
                "      <input type=\"submit\" value=\"Сохранить\"/>" +
                "    </form>" +
                HtmlBuilder.buildEnd();
    }

    /**
     * Редактирует элемент списка на основе полученных данных.
     * @param itemId индекс элемента списка.
     * @return перенаправление на основную страницу со списком.
     */
    @POST
    @Path("/edit/{id}")
    @Produces("text/html")
    public Response editItem(@PathParam("id") int itemId, @FormParam("value") String itemValue) {
        list.set(itemId, itemValue);
        try {
            return Response.seeOther(new URI("/")).build();
        } catch (URISyntaxException e) {
            throw new IllegalStateException(Constants.URI_SYNTAX_ERROR_MESSAGE);
        }
    }

    /**
     * Пример вывода вложенного списка.
     */
    @GET
    @Path("nested_list")
    @Produces("text/html")
    public String getNestedListExample() {
        return HtmlBuilder.buildStart("Hello world")+
                "    <h1>Hello world</h1>" +
                "    <ul>" +
                "      <li>1</li>" +
                "      <li>2</li>" +
                "      <li>3" +
                "        <ul>" +
                "          <li>3.1</li>" +
                "        </ul>" +
                "      </li>" +
                "    </ul>" +
                HtmlBuilder.buildEnd();
    }

}
