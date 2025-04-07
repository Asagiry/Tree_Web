package ru.krista.yargu.epishin.web.tree;

import ru.krista.yargu.epishin.tree.Node;
import ru.krista.yargu.epishin.tree.NodeStorage;
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
import java.util.UUID;

/**
 * Контроллер отвечающий за представление списка.
 */
@Path(Constants.TREE_BASE_PATH)
public class TreePresentationController {
    private final NodeStorage treeStorage;

    /**
     * Запоминает дерево, с которым будет работать.
     * @param nodeStorage список, с которым будет работать контроллер.
     */
    public TreePresentationController(NodeStorage nodeStorage) {
        treeStorage = nodeStorage;
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

        resultBuilder.append(HtmlBuilder.buildStart("Вывод дерева"))
                .append("    <h1>Дерево</h1>")
                .append("    <ul>");
        treeStorage.getTree().iterateTree((level, node) -> resultBuilder.append("<br>")
                .append(" <a href=\"").append(Constants.TREE_BASE_PATH).append("/edit/")
                .append(node.getId())
                .append("\" style=\"color: blue; text-decoration: none;\">")  // Фиксированный стиль
                .append("____".repeat(level))
                .append(node.getName())
                .append("</a>"));
        resultBuilder.append("</ul>")
                .append("      <br/>")
                .append(HtmlBuilder.buildFormPost(Constants.TREE_ADD_PATH,"","Добавить элемент к корню"))
                .append(HtmlBuilder.buildEnd());

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
        treeStorage.getTree().addChild(new Node("ZZZ"));
        treeStorage.save();
        try {
            return Response.seeOther(new URI(Constants.TREE_BASE_PATH)).build();
        } catch (URISyntaxException e) {
            throw new IllegalStateException(Constants.URI_SYNTAX_ERROR_MESSAGE);
        }
    }
    @POST
    @Path("add/{parentId}")
    @Produces("text/html")
    public Response add(@PathParam("parentId") UUID parentId) {
        Node parent = treeStorage.getTree().findChildById(parentId);
        parent.addChild(new Node("ZZZ"));
        treeStorage.save();
        try {
            return Response.seeOther(new URI(Constants.TREE_BASE_PATH)).build();
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
    public String getEditPage(@PathParam("id") UUID itemId) {
        Node treeItem = treeStorage.getTree().findChildById(itemId);
        StringBuilder result = new StringBuilder();
        result.append(HtmlBuilder.buildStart("Редактирование элемента дерева"))
                .append("    <h1>Редактирование элемента дерева</h1>")
                .append("    <form method=\"post\" action=\"").append(Constants.TREE_BASE_PATH).append("/edit/").append(itemId).append("\">")
                .append("      <p>Значение</p>")
                .append("      <input type=\"text\" name=\"value\" value=\"").append(treeItem.getName()).append("\"/>")
                .append("      <input type=\"submit\" value=\"Сохранить\"/>")
                .append("    </form>")
                .append(HtmlBuilder.buildFormPost(Constants.TREE_ADD_PATH,itemId.toString(),"Добавить элемент"));
        if (!itemId.equals(treeStorage.getTree().getId())) {
            result.append(HtmlBuilder.buildFormPost(Constants.TREE_DELETE_PATH,itemId.toString(),"Удалить элемент"));
        }
        result.append(HtmlBuilder.buildEnd());

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
        Node edited = treeStorage.getTree().findChildById(itemId);
        edited.setName(name);
        treeStorage.save();
        try {
            return Response.seeOther(new URI(Constants.TREE_BASE_PATH)).build();
        } catch (URISyntaxException e) {
            throw new IllegalStateException(Constants.URI_SYNTAX_ERROR_MESSAGE);
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
        treeStorage.getTree().deleteChildById(itemId);
        treeStorage.save();
        try {
            return Response.seeOther(new URI(Constants.TREE_BASE_PATH)).build();
        } catch (URISyntaxException e) {
            throw new IllegalStateException(Constants.URI_SYNTAX_ERROR_MESSAGE);
        }

    }

}
