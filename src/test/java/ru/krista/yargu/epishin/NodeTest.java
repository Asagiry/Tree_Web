package ru.krista.yargu.epishin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import ru.krista.yargu.epishin.tree.Node;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;


class NodeTest {

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
    void createParamTree(){
        String name = "template";
        UUID id = UUID.randomUUID();
        List<Node> children = List.of(
                new Node("a"),
                new Node("b"),
                new Node("c")
        );

        Node test = new Node(name,id,children);

        assertEquals(name,test.getName());
        assertEquals(id,test.getId());
        assertEquals(children,test.getChildren());
    }

    @Test
    void setName(){
        Node root = new Node("Root");
        root.setName("NewRoot");

        assertEquals("NewRoot",root.getName());
    }

    @Test
    void setId(){
        Node root = new Node("Root");
        UUID newUUID = UUID.randomUUID();
        root.setId(newUUID);

        assertEquals(newUUID,root.getId());
    }

    @Test
    void setChildren(){
        Node root = new Node("Root");
        Node a = new Node("a");
        Node b = new Node("b");

        List<Node> children = List.of(a,b);
        root.setChildren(children);

        assertEquals(children,root.getChildren());
    }

    @Test
    void addNode(){
        Node root = new Node("Root");
        Node child = new Node("Child");
        root.addChild(child);
        assertEquals(child.getId(),root.getChildren().get(0).getId());
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
    void addNodeThatAlreadyChild(){
        Node root = new Node("Root");
        Node child = new Node("Child");
        root.addChild(child);

        root.addChild(child);
        assertEquals(1,root.getChildren().size());
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
    void findChildById(){
        Node root = new Node("Root");
        Node firstChildren = new Node("FirstChildren");
        Node secondChildren = new Node("SecondChildren");
        root.addChild(firstChildren);
        root.addChild(secondChildren);

        Node found = root.findChildById(firstChildren.getId());
        assertEquals(firstChildren.getId(),found.getId());

        found = root.findChildById(secondChildren.getId());
        assertEquals(secondChildren.getId(),found.getId());
    }

    @Test
    void findDeepChildById(){
        Node root = new Node("Root");
        Node a = new Node("a");
        Node b = new Node("b");
        Node c = new Node("c");
        Node d = new Node("d");
        Node e = new Node("e");
        Node f = new Node("f");
        root.addChild(a);
        root.addChild(b);
        a.addChild(c);
        a.addChild(d);
        b.addChild(e);
        b.addChild(f);

        Node foundE = root.findChildById(e.getId());
        Node foundF = root.findChildById(f.getId());

        assertEquals(foundE,e);
        assertEquals(foundF,f);
    }

    @Test
    void findNonExistChildById(){
        Node root = new Node("Root");

        Node notFound = root.findChildById(UUID.randomUUID());

        assertNull(notFound);
    }

    @Test
    void findFatherById(){
        Node root = new Node("Root");
        Node a = new Node("a");
        Node b = new Node("b");
        Node c = new Node("c");
        Node d = new Node("d");
        Node e = new Node("e");
        Node f = new Node("f");
        root.addChild(a);
        root.addChild(b);
        a.addChild(c);
        a.addChild(d);
        b.addChild(e);
        b.addChild(f);

        Node fatherC = root.findFatherById(c.getId());
        Node fatherD = root.findFatherById(d.getId());
        Node fatherE = root.findFatherById(e.getId());
        Node fatherF = root.findFatherById(f.getId());
        Node fatherA = root.findFatherById(a.getId());
        Node fatherB = root.findFatherById(b.getId());
        Node fatherRoot = root.findFatherById(root.getId());

        assertEquals(a,fatherC);
        assertEquals(a,fatherD);
        assertEquals(b,fatherE);
        assertEquals(b,fatherF);
        assertEquals(root,fatherA);
        assertEquals(root,fatherB);
        assertNull(fatherRoot);


    }

    @Test
    void deleteChildById(){
        Node root = new Node("Root");
        Node a = new Node("a");
        Node b = new Node("b");
        root.addChild(a);
        a.addChild(b);
        root.deleteChildById(b.getId());

        assertEquals(0,a.getChildren().size());
        assertEquals(1,root.getChildren().size());

        root.deleteChildById(a.getId());

        assertEquals(0,root.getChildren().size());
    }

    @Test
    void iterateTest(){
        Node root = new Node("Root");
        Node a = new Node("a");
        Node b = new Node("b");
        Node c = new Node("c");
        Node d = new Node("d");
        Node e = new Node("e");
        Node f = new Node("f");
        root.addChild(a);
        root.addChild(b);
        a.addChild(c);
        a.addChild(d);
        b.addChild(e);
        b.addChild(f);

        AtomicInteger count = new AtomicInteger(0);
        root.iterateTree((level, node)->{
            if (node.getName().equals("c")) {
                assertEquals(2,level);
            }
            count.incrementAndGet();
        });
        assertEquals(7,count.get());
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
        String expected = """
                Root
                  a
                    c
                    d
                  b
                    e
                """;

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
        String expected = """
                <HTML>
                <HEAD>
                <BODY>
                Root<br>  a<br>    c<br>    d<br>  b<br>    e<br></BODY>
                </HEAD>
                </HTML>""";
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

    @Test
    void getJsonTreeTest() throws JsonProcessingException {
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

        String actual = root.getJsonTree();

        String expected = new ObjectMapper().
                writerWithDefaultPrettyPrinter().
                writeValueAsString(root);

        assertEquals(expected,actual);

    }

    @Test
    void writeJsonToFileTest() throws IOException {
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

        String fileName = "testJson";

        root.writeJsonFileTree(fileName);

        Boolean isFileCreated = Files.exists(Path.of(fileName+".json"));
        assertEquals(true,isFileCreated);

        String actual = Files.readString(Path.of(fileName+".json"));
        assertEquals(root.getJsonTree(),actual);

        Files.delete(Path.of(fileName+".json"));

    }

    @Test
    void readJsonFileTest() throws IOException {
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

        String fileName = "testJson";
        root.writeJsonFileTree(fileName);

        Node equalsRoot = Node.readJsonFileTree(fileName);

        assertEquals(root,equalsRoot);

        Files.delete(Path.of(fileName+".json"));

    }

}
