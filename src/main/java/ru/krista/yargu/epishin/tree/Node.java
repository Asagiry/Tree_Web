package ru.krista.yargu.epishin.tree;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public class Node {
    private String name;
    private UUID id;
    private List<Node> children;

    public Node(String newName) {
        name = newName;
        id = UUID.randomUUID();
        children = new ArrayList<>();
    }

    public Node(@JsonProperty("name") String name,
                @JsonProperty("id") UUID id,
                @JsonProperty("children") List<Node> children) {
        this.name = name != null ? name : "NewNode";
        this.id = id != null ? id : UUID.randomUUID();
        this.children = children != null ? children : new ArrayList<>();
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

    public Node findChildById(UUID id) {
        AtomicReference<Node> founded = new AtomicReference<>();
        iterateTree((level, node) -> {
            if (node.getId().equals(id)) {
                founded.set(node);
            }
        });
        return founded.get();
    }

    public Node findFatherById(UUID id) {
        AtomicReference<Node> result = new AtomicReference<>();

        iterateTree((level, node) -> {
            for (Node child : node.getChildren()) {
                if (id.equals(child.getId())) {
                    result.set(node);
                    return;
                }
            }
        });
        return result.get();
    }

    public void deleteChildById(UUID id){
        Node father = findFatherById(id);
        if (father!=null){
            Node toDelete = findChildById(id);
            List<Node> fatherChildren = father.getChildren();
            fatherChildren.remove(toDelete);
            father.setChildren(fatherChildren);
            toDelete.removeChildren();
        }
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
            output.append("  ".repeat(Math.max(0, level)));
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
    public String getJsonTree() throws JsonProcessingException{
        return new
                ObjectMapper().writerWithDefaultPrettyPrinter().
                writeValueAsString(this);
    }

    public void writeHtmlFileTree(String fileName) throws IOException{
            String text = getHtmlTree();
            Path filePath = Path.of(fileName+".html");
            Files.writeString(filePath, text);
    }

    public void writeJsonFileTree(String fileName) throws IOException{
            String text = getJsonTree();
            Path filePath = Path.of(fileName+".json");
            Files.writeString(filePath, text);
    }

    public static Node readJsonFileTree(String fileName) throws IOException{
            String file = Files.readString(Path.of(fileName+".json"));
            return new ObjectMapper().readValue(file, Node.class);
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

