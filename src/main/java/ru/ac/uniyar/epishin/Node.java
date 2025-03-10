package ru.ac.uniyar.epishin;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Node {
    private String _name;
    private final UUID _id;
    private final List<Node> _children;

    public Node(String name) {
        _name = name;
        _id = UUID.randomUUID();
        _children = new ArrayList<Node>();
    }

    public String getName() {
        return _name;
    }

    public UUID getId() {
        return _id;
    }

    public List<Node> getChildren() {
        return _children;
    }

    public void addChild(Node child)  {

        if (child.getChildren().contains(this))
            throw new IllegalArgumentException("Given node is already father for this node");


        if (!_children.contains(child)){
            _children.add(child);
        }
    }

    public void removeChildren() {
        _children.clear();
    }

    public void removeChildByName(String name) {
        for (int i = 0;i!= _children.size();i++){
            if (_children.get(i).getName().equals(name))
            {
                _children.remove(i);
                break;
            }
        }
    }

    public void removeChildById(UUID id) {
        for (int i = 0;i!= _children.size();i++){
            if (_children.get(i).getId().toString().equals(id.toString()))
            {
                _children.remove(i);
                break;
            }
        }
    }

    public Node findChild(String name) {
        for (int i = 0;i!= _children.size();i++){
            if (_children.get(i).getName().equals(name))
            {
                return _children.get(i);
            }
        }
        return null;
    }

    public void editName(String name) {
        _name = name;
    }

    public void iterateTree(TreeIteratorHandler handler){
        _iterateStaticTree(handler,this,0);
    }

    private static void _iterateStaticTree(TreeIteratorHandler handler, Node node,int level){
        handler.handleNode(level, node);

        List<Node> children = node.getChildren();
        for (int i = 0;i!=children.size();i++){
            Node._iterateStaticTree(handler,children.get(i),level+1);
        }
    }

    public String getStringTree(){
        final StringBuilder output = new StringBuilder();

        iterateTree((level, node)->{
            output.append("＿＿＿＿".repeat(Math.max(0, level)));
            output.append(node.getName());
            output.append('\n');
        });
        return output.toString();
    }

    public String getHtmlTree(){
        final StringBuilder output = new StringBuilder();
        output.append(generateStartHtml());

        iterateTree((level, node)->{
            output.append("＿＿＿＿".repeat(Math.max(0, level)));
            output.append(node.getName());
            output.append("<br>");
        });

        output.append(generateEndHtml());

        return output.toString();
    }

    private String generateStartHtml() {
        return "<HTML>\n" +
                "<HEAD>\n" +
                "<BODY>\n";
    }
    private String generateEndHtml() {
        return "</BODY>\n" +
                "</HEAD>\n" +
                "</HTML>";
    }

    public void writeHtmlFileTree(String fileName){
        try {
            String text = getHtmlTree();
            Path filePath = Path.of(fileName+".html");
            Files.writeString(filePath, text);
        } catch (IOException e) {
            System.out.println("Ошибка записи в файл: " + e.getMessage());
        }
    }

}

