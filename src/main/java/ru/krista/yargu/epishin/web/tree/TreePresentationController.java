package ru.krista.yargu.epishin.web.tree;

import ru.krista.yargu.epishin.tree.Node;
import ru.krista.yargu.epishin.web.utils.Constants;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

/**
 * Контроллер отвечающий за представление списка.
 */
@Path(Constants.TREE_BASE_PATH)
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
    public String getTree() {
        StringBuilder resultBuilder = new StringBuilder();

        resultBuilder.append("<html>")
                .append("  <head>")
                .append("    <title>Вывод дерева</title>")
                .append("  </head>")
                .append("  <body>")
                .append("    <h1>Дерево</h1>")
                .append("    <ul>");
        tree.iterateTree((level, node) -> resultBuilder.append("<br>")
                .append(" <a href=\"").append(Constants.TREE_BASE_PATH).append("/edit/")
                .append(node.getId())
                .append("\" style=\"color: blue; text-decoration: none;\">")  // Фиксированный стиль
                .append("____".repeat(level))
                .append(node.getName())
                .append("</a>"));
        resultBuilder.append("</ul>")
                .append("      <br/>")
                .append("      <form method=\"post\" action=\"").append(Constants.TREE_BASE_PATH).append("/add/\">")
                .append("        <input type=\"submit\" value=\"Добавить элемент к корню\"/>")
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
    @Path("add/")
    @Produces("text/html")
    public Response add(){
        tree.addChild(new Node("ZZZ"));
        try {
            return Response.seeOther(new URI(Constants.TREE_BASE_PATH)).build();
        } catch (URISyntaxException e) {
            throw new IllegalStateException("Ошибка построения URI для перенаправления");
        }
    }
    @POST
    @Path("add/{parentId}")
    @Produces("text/html")
    public Response add(@PathParam("parentId") UUID parentId) {
        Node parent = tree.findChildById(parentId);
        parent.addChild(new Node("ZZZ"));
        try {
            return Response.seeOther(new URI(Constants.TREE_BASE_PATH)).build();
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
    public String getEditPage(@PathParam("id") UUID itemId) {
        Node treeItem = tree.findChildById(itemId);
        StringBuilder result = new StringBuilder();

        result.append("<html>")
                .append("  <head>")
                .append("    <title>Редактирование элемента дерева</title>")
                .append("  </head>")
                .append("  <body>")
                .append("    <h1>Редактирование элемента дерева</h1>")
                .append("    <form method=\"post\" action=\"").append(Constants.TREE_BASE_PATH).append("/edit/").append(itemId).append("\">")
                .append("      <p>Значение</p>")
                .append("      <input type=\"text\" name=\"value\" value=\"").append(treeItem.getName()).append("\"/>")
                .append("      <input type=\"submit\" value=\"Сохранить\"/>")
                .append("    </form>")
                .append("    <form method=\"post\" action=\"").append(Constants.TREE_BASE_PATH).append("/add/").append(itemId).append("\">")
                .append("      <input type=\"submit\" value=\"Добавить элемент\"/>")
                .append("    </form>");
        if (!itemId.equals(tree.getId())) {
            result.append("    <form method=\"post\" action=\"").append(Constants.TREE_BASE_PATH).append("/delete/").append(itemId).append("\">")
                    .append("      <input type=\"submit\" value=\"Удалить элемент\"/>")
                    .append("    </form>");
        }
        result.append("  </body>")
                .append("</html>");

        return result.toString();
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
            return Response.seeOther(new URI(Constants.TREE_BASE_PATH)).build();
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
        tree.deleteChildById(itemId);
        try {
            return Response.seeOther(new URI(Constants.TREE_BASE_PATH)).build();
        } catch (URISyntaxException e) {
            throw new IllegalStateException("Ошибка построения URI для перенаправления");
        }

    }

}
