package ru.krista.yargu.epishin.web;

import ru.krista.yargu.epishin.tree.Node;
import ru.krista.yargu.epishin.web.list.ListPresentationController;
import ru.krista.yargu.epishin.web.login.LoginController;
import ru.krista.yargu.epishin.web.tree.TreePresentationController;
import ru.krista.yargu.epishin.web.utils.Constants;

import javax.ws.rs.core.Application;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Web-приложение в котором регистрируются все ресурсы.
 */
public class RestApplication extends Application {

    private final List<String> list = new ArrayList<>();
    private Node tree = new Node("tree");

    private ListPresentationController listPresentationController;
    private TreePresentationController treePresentationController;
    private final LoginController loginController = new LoginController();

    public RestApplication() {
        listInit();
        treeInit();
    }

    private void listInit(){
        list.add("aaa");
        list.add("bbb");
        list.add("ccc");
        list.add("ddd");

        listPresentationController = new ListPresentationController(list);
    }

    private void treeInit() {
        try {
            tree = Node.readJsonFileTree(Constants.TREE_FILE_PATH);
        } catch (IOException exception) {
            System.out.println("Не удалось загрузить дерево с файла "+ Constants.TREE_FILE_PATH);

            Node a = new Node("a");
            Node b = new Node("b");
            Node c = new Node("c");
            Node d = new Node("d");
            Node e = new Node("e");
            Node f = new Node("f");

            tree.addChild(a);
            tree.addChild(b);
            a.addChild(c);
            a.addChild(d);
            b.addChild(e);
            b.addChild(f);
        }
        treePresentationController = new TreePresentationController(tree);

    }

    /**
     * Возвращает список всех ресурсов web-приложения.
     * @return список всех ресурсов web-приложения.
     */
    @Override
    public Set<Object> getSingletons() {
        Set<Object> resources = new HashSet<>();
        resources.add(listPresentationController);
        resources.add(treePresentationController);
        resources.add(loginController);
        return resources;
    }

    public Node getTree(){
        return tree;
    }
    public List<String> getList(){
        return list;
    }

}
