package ru.krista.yargu.epishin.web;

import ru.krista.yargu.epishin.tree.Node;
import ru.krista.yargu.epishin.web.list.ListPresentationController;
import ru.krista.yargu.epishin.web.login.LoginController;
import ru.krista.yargu.epishin.web.tree.TreePresentationController;

import javax.ws.rs.core.Application;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Web-приложение в котором регистрируются все ресурсы.
 */
public class RestApplication extends Application {

    private final List<String> list = new ArrayList<>();
    private final Node tree = new Node("tree");

    public RestApplication() {
        listInit();
        treeInit();
    }

    private void listInit(){
        list.add("aaa");
        list.add("bbb");
        list.add("ccc");
        list.add("ddd");
    }

    private void treeInit(){
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

    /**
     * Возвращает список всех ресурсов web-приложения.
     * @return список всех ресурсов web-приложения.
     */
    @Override
    public Set<Object> getSingletons() {
        Set<Object> resources = new HashSet<>();
        resources.add(new ListPresentationController(list));
        resources.add(new TreePresentationController(tree));
        resources.add(new LoginController());
        return resources;
    }
}
