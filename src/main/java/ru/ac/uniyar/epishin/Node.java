package ru.ac.uniyar.epishin;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Node {
    private String name;
    private UUID id;
    private List<Node> children;

    public Node(String newName) {
        name = newName;
        id = UUID.randomUUID();
        children = new ArrayList<>();
    }

    public Node(){
        name = "NewNode";
        id = UUID.randomUUID();
        children = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public UUID getId() {
        return id;
    }

    public List<Node> getChildren() {
        return children;
    }

    public void setName(String newName){
        name = newName;
    }

    public void setId(UUID newId){
        id = newId;
    }

    public void setChildren(List<Node> newChildren)
    {
        children = newChildren;
    }



    public void addChild(Node child)  {

        if (child.getChildren().contains(this))
            throw new IllegalArgumentException("Given node is already father for this node");


        if (!children.contains(child)){
            children.add(child);
        }
    }

    public void removeChildren() {
        children.clear();
    }

    public void removeChildByName(String name) {
        for (int i = 0;i!= children.size();i++){
            if (children.get(i).getName().equals(name))
            {
                children.remove(i);
                break;
            }
        }
    }

    public void removeChildById(UUID id) {
        for (int i = 0;i!= children.size();i++){
            if (children.get(i).getId().toString().equals(id.toString()))
            {
                children.remove(i);
                break;
            }
        }
    }

    public Node findChild(String name) {
        for (int i = 0;i!= children.size();i++){
            if (children.get(i).getName().equals(name))
            {
                return children.get(i);
            }
        }
        return null;
    }

    public void iterateTree(TreeIteratorHandler handler){
        iterateStaticTree(handler,this,0);
    }

    private static void iterateStaticTree(TreeIteratorHandler handler, Node node,int level){
        handler.handleNode(level, node);

        List<Node> children = node.getChildren();
        for (int i = 0;i!=children.size();i++){
            Node.iterateStaticTree(handler,children.get(i),level+1);
        }
    }
    @JsonIgnore
    public String getStringTree(){
        final StringBuilder output = new StringBuilder();
        iterateTree((level, node)->{
            output.append("＿＿＿＿".repeat(Math.max(0, level)));
            output.append(node.getName());
            output.append('\n');
        });
        return output.toString();
    }
    @JsonIgnore
    public String getHtmlTree(){
        String stringTree = getStringTree();
        return "<HTML>\n" +
                "<HEAD>\n" +
                "<BODY>\n" +
                stringTree.replace("\n","<br>")+
                "</BODY>\n"+
                "</HEAD>\n"+
                "</HTML>";
    }

    @JsonIgnore
    public String getJsonTree(){
        try {
            return new
                    ObjectMapper().writerWithDefaultPrettyPrinter().
                    writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Ошибка преобразования в JSON"+e.getMessage());
        }
    }

    public void writeHtmlFileTree(String fileName){
        try {
            String text = getHtmlTree();
            Path filePath = Path.of(fileName+".html");
            Files.writeString(filePath, text);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка записи в файл: " + e.getMessage());
        }
    }

    public void writeJsonFileTree(String fileName){
        try {
            String text = getJsonTree();
            Path filePath = Path.of(fileName+".json");
            Files.writeString(filePath, text);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка записи в файл: " + e.getMessage());
        }
    }

    public static Node readJsonFileTree(String fileName){
        try {
            String file = Files.readString(Path.of(fileName+".json"));
            return new ObjectMapper().readValue(file, Node.class);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка считывания файла: " + e.getMessage());
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node that = (Node) o;
        return getName().equals(that.getName()) &&
                getId().equals(that.getId()) &&
                getChildren().equals(that.getChildren());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, id, children);
    }
}

