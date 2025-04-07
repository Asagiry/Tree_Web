package ru.krista.yargu.epishin.web;

import ru.krista.yargu.epishin.tree.Node;
import ru.krista.yargu.epishin.tree.NodeStorage;
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
    private final NodeStorage treeStorage = new NodeStorage();


    public RestApplication() {
        listInit();
    }

    private void listInit(){
        list.add("aaa");
        list.add("bbb");
        list.add("ccc");
        list.add("ddd");
    }


    /**
     * Возвращает список всех ресурсов web-приложения.
     * @return список всех ресурсов web-приложения.
     */
    @Override
    public Set<Object> getSingletons() {
        Set<Object> resources = new HashSet<>();
        resources.add(new ListPresentationController(list));
        resources.add(new TreePresentationController(treeStorage));
        resources.add(new LoginController());
        return resources;
    }

}
