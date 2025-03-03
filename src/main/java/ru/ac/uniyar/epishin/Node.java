package ru.ac.uniyar.epishin;

import javax.swing.tree.TreeCellRenderer;
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

    public void iterateTree(TreeIteratorHandler handler){ // Можно сделать через static но я не уверен как лучше
        handler.handleNode(0,this);

        List<Node> children = this.getChildren();
        for(int i =0;i!=children.size();i++){
            iterateTree(handler,children.get(i),1);
        }
    }

    private void iterateTree(TreeIteratorHandler handler, Node node, int level) {
        handler.handleNode(level,node);

        List<Node> children = node.getChildren();
        for(int i =0;i!=children.size();i++){
            iterateTree(handler,children.get(i),level+1);
        }
    }

    public static void iterateStaticTree(TreeIteratorHandler handler, Node node,int level){
        handler.handleNode(level, node);

        List<Node> children = node.getChildren();
        for (int i = 0;i!=children.size();i++){
            Node.iterateStaticTree(handler,children.get(i),level+1);
        }
    }


}
