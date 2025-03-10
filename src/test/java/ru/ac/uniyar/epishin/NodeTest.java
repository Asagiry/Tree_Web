package ru.ac.uniyar.epishin;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicInteger;

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

    /*
    root
        a
            c
            d
        b
            e
     */
    @Test
    void iterateTest(){
        Node root = new Node("Root");
        Node a = new Node("a");
        Node b = new Node("b");
        Node c = new Node("c");
        Node d = new Node("d");
        Node e = new Node("e");
        root.addChild(a);
        root.addChild(b);
        a.addChild(c);
        a.addChild(d);
        b.addChild(e);

        AtomicInteger count = new AtomicInteger(0);
        root.iterateTree((level, node)->{
            if (node.getName().equals("c")) {
                assertEquals(2,level);
            }
            count.incrementAndGet();
        });
        assertEquals(6,count.get());
    }
    @Test
    void getStringTreeTest() {
        Node root = new Node("Root");
        Node a = new Node("a");
        Node b = new Node("b");
        Node c = new Node("c");
        Node d = new Node("d");
        Node e = new Node("e");
        root.addChild(a);
        root.addChild(b);
        a.addChild(c);
        a.addChild(d);
        b.addChild(e);

        String actual = root.getStringTree();
        String expected = "Root\n" +
                "＿＿＿＿a\n" +
                "＿＿＿＿＿＿＿＿c\n" +
                "＿＿＿＿＿＿＿＿d\n" +
                "＿＿＿＿b\n" +
                "＿＿＿＿＿＿＿＿e\n";

        assertEquals(expected,actual);
    }

    @Test
    void getHtmlTreeTest() {
        Node root = new Node("Root");
        Node a = new Node("a");
        Node b = new Node("b");
        Node c = new Node("c");
        Node d = new Node("d");
        Node e = new Node("e");
        root.addChild(a);
        root.addChild(b);
        a.addChild(c);
        a.addChild(d);
        b.addChild(e);

        String actual = root.getHtmlTree();
        String expected = "<HTML>\n" +
                "<HEAD>\n" +
                "<BODY>\n" +
                "Root<br>＿＿＿＿a<br>＿＿＿＿＿＿＿＿c<br>＿＿＿＿＿＿＿＿d<br>＿＿＿＿b<br>＿＿＿＿＿＿＿＿e<br></BODY>\n" +
                "</HEAD>\n" +
                "</HTML>";
        assertEquals(expected,actual);
    }

    @Test
    void writeHtmlToFileTest() throws IOException {
        Node root = new Node("Root");
        Node a = new Node("a");
        Node b = new Node("b");
        Node c = new Node("c");
        Node d = new Node("d");
        Node e = new Node("e");
        root.addChild(a);
        root.addChild(b);
        a.addChild(c);
        a.addChild(d);
        b.addChild(e);

        String fileName = "testHtml";
        
        root.writeHtmlFileTree(fileName);

        Boolean isFileCreated = Files.exists(Path.of(fileName+".html"));
        assertEquals(true,isFileCreated);

        String actual = Files.readString(Path.of(fileName+".html"));
        assertEquals(root.getHtmlTree(),actual);

        Files.delete(Path.of(fileName+".html"));

    }

}
