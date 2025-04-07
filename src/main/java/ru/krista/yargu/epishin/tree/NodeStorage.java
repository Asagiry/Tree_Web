package ru.krista.yargu.epishin.tree;

import ru.krista.yargu.epishin.web.utils.Constants;

import java.io.IOException;

public class NodeStorage{
    private Node tree = null;

    public NodeStorage(){
        if (Constants.SAVE_TO_FILE)
            initByFile();
        else
            initByDB();
    }

    private void initByFile(){
        try {
            tree = Node.readJsonFileTree(Constants.TREE_FILE_PATH);
        } catch (IOException e) {
            initBase();
        }
    }

    private void initBase(){
        tree = new Node("tree");

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

    private void initByDB(){
        //NOT IMPLEMENTED
    }

    public void save(){
        if (Constants.SAVE_TO_FILE)
            saveToFile();
        else
            saveToDB();
    }

    private void saveToFile(){
        try {
            tree.writeJsonFileTree(Constants.TREE_FILE_PATH);
        } catch (IOException e) {
            System.out.println("Не удалось сохранить дерево");
        }
    }
    private void saveToDB(){
        //Not implemented
    }

    public Node getTree(){
        return tree;
    }

}
