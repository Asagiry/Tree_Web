package ru.ac.uniyar.epishin;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class NodeTest {

    /*Реализовать структуру данных хранящую в себе дерево
- Использовать ООП
- Каждый узел хранит строку и идентификатор
- Реализовать операции
- создание дерева
- добавление узла в дерево
- удаление дочернего узла по его имени или идентификатору
- удаление всех дочерних узлов
- поиск дочернего узла по имени
- изменение значения узла
- Main функция не нужна. Это будет библиотека
     */


    @Test
    void createTree(){
        Node root = new Node("Root");
        assertNotNull(root);
        assertEquals("Root",root.getName());
    }

    @Test
    void addNode(){
        Node root = new Node("Root");
        Node child = new Node("Child");
        root.addChild(child);
        assertEquals(child.getId(),root.getChildren().get(0).getId());
    }

    @Test
    void addNodeThatAlreadyChild(){
        Node root = new Node("Root");
        Node child = new Node("Child");
        root.addChild(child);

        root.addChild(child);
        assertEquals(1,root.getChildren().size());
    }

    @Test
    void addNodeToChild(){
        Node father = new Node("Father");
        Node child = new Node("Child");
        father.addChild(child);

        assertThrows(Exception.class, () -> {
            child.addChild(father);
        });
    }

    @Test
    void removeChildByName() {
        Node root = new Node("Root");
        Node child = new Node("Child");
        Node remove = new Node("Remove");
        root.addChild(child);
        root.addChild(remove);

        root.removeChildByName("Remove");

        assertEquals(1,root.getChildren().size());
        assertEquals(child.getName(),root.getChildren().get(0).getName());
    }

    @Test
    void removeChildById(){
        Node root = new Node("Root");
        Node child = new Node("Child");
        Node remove = new Node("Remove");
        root.addChild(child);
        root.addChild(remove);

        root.removeChildById(remove.getId());

        assertEquals(1,root.getChildren().size());
        assertEquals(child.getId(),root.getChildren().get(0).getId());
    }

    @Test
    void removeAllChildren(){
        Node root = new Node("Root");
        Node firstChild = new Node("FirstChild");
        Node secondChild = new Node("SecondChild");
        root.addChild(firstChild);
        root.addChild(secondChild);

        root.removeChildren();

        assertEquals(0,root.getChildren().size());
    }

    @Test
    void findChild(){
        Node root = new Node("Root");
        Node firstChildren = new Node("FirstChildren");
        Node secondChildren = new Node("SecondChildren");
        root.addChild(firstChildren);
        root.addChild(secondChildren);

        Node found = root.findChild(firstChildren.getName());
        assertEquals(firstChildren.getId(),found.getId());

        found = root.findChild(secondChildren.getName());
        assertEquals(secondChildren.getId(),found.getId());

    }

    @Test
    void findNotExistChild(){
        Node root = new Node("Root");

        Node notFount = root.findChild("Child");

        assertNull(notFount);
    }

    @Test
    void editName(){
        Node root = new Node("Root");
        root.editName("NewRoot");

        assertEquals("NewRoot",root.getName());
    }

}
