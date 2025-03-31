package ru.krista.yargu.epishin.web.tree;

import com.fasterxml.jackson.core.JsonProcessingException;
import ru.krista.yargu.epishin.Node;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Контроллер отвечающий за представление списка.
 */
@Path("/tree")
public class TreePresentationController {
    private final Node tree;

    /**
     * Запоминает дерево, с которым будет работать.
     * @param tree список, с которым будет работать контроллер.
     */
    public TreePresentationController(Node tree) {
        this.tree = tree;
    }

    /**
     * Выводит HTML-страницу со списком, ссылками на страницы редактирования и копкой добавления записи.
     * @return HTML-страница со списком.
     */
    @GET
    @Path("/")
    @Produces("text/html")
    public String getList() throws IOException {
        // Используем StringBuilder для эффективного построения строки
        StringBuilder resultBuilder = new StringBuilder();

        resultBuilder.append("<html>")
                .append("  <head>")
                .append("    <title>Вывод дерева</title>")
                .append("  </head>")
                .append("  <body>")
                .append("    <h1>Дерево</h1>")
                .append("    <ul>");
        // Обходим дерево и добавляем элементы
        tree.iterateTree((level, node) -> {
            resultBuilder.append("<br>")
                    .append(" <a href=\"/tree/edit/")
                    .append(node.getId())
                    .append("\">")
                    .append("＿＿".repeat(level))
                    .append(node.getName())
                    .append("</a>");
        });
        resultBuilder.append("</ul>")
                .append("      <br/>")
                .append("      <form method=\"post\" action=\"/tree/add\">")
                .append("        <input type=\"submit\" value=\"Add\"/>")
                .append("      </form>")
                .append("  </body>")
                .append("</html>");

        return resultBuilder.toString();
    }

    /**
     * Добавляет одну случайную запись в список и перенаправляет пользователя на основную страницу со списком.
     * @return перенаправление на основную страницу со списком.
     */
    @POST
    @Path("add")
    @Produces("text/html")
    public Response addRandomItem() {
        tree.addChild(new Node("zzz"));
        try {
            return Response.seeOther(new URI("/tree")).build();
        } catch (URISyntaxException e) {
            throw new IllegalStateException("Ошибка построения URI для перенаправления");
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
    public String getEditPage(@PathParam("id") String itemId) {
        Node treeItem = tree.findChildById(UUID.fromString(itemId));
        String result =
                "<html>" +
                        "  <head>" +
                        "    <title>Редактирование элемента списка</title>" +
                        "  </head>" +
                        "  <body>" +
                        "    <h1>Редактирование элемента списка</h1>" +
                        "    <form method=\"post\" action=\"/tree/edit/" + itemId + "\">" +
                        "      <p>Значение</p>" +
                        "      <input type=\"text\" name=\"value\" value=\"" + treeItem.getName() + "\"/>" +
                        "      <input type=\"submit\" value=\"Сохранить\"/>" +
                        "    </form>" +
                        "    <form method=\"post\" action=\"/tree/delete/" + itemId + "\">" +
                        "      <input type=\"submit\" value=\"Удалить элемент\"/>" +
                        "    </form>" +
                        "  </body>" +
                        "</html>";
        return result;
    }

    /**
     * Редактирует элемент списка на основе полученных данных.
     * @param itemId индекс элемента списка.
     * @return перенаправление на основную страницу со списком.
     */
    @POST
    @Path("/edit/{id}")
    @Produces("text/html")
    public Response editItem(@PathParam("id") UUID itemId, @FormParam("value") String name) {
        Node edited = tree.findChildById(itemId);
        edited.setName(name);
        try {
            return Response.seeOther(new URI("/tree")).build();
        } catch (URISyntaxException e) {
            throw new IllegalStateException("Ошибка построения URI для перенаправления");
        }
    }

    /**
     * Удаляет элемент списка на основе полученных данных.
     * @param itemId индекс элемента списка.
     * @return перенаправление на основную страницу со списком.
     */
    @POST
    @Path("/delete/{id}")
    @Produces("text/html")
    public Response deleteItem(@PathParam("id") UUID itemId) {
        Node father = tree.findFatherById(itemId);
        List<Node> children = father.getChildren();
        children.remove(father.findChildById(itemId));
        father.setChildren(children);
        try {
            return Response.seeOther(new URI("/tree")).build();
        } catch (URISyntaxException e) {
            throw new IllegalStateException("Ошибка построения URI для перенаправления");
        }

    }

}
