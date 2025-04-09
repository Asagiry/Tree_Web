package ru.krista.yargu.epishin.tree;

import ru.krista.yargu.epishin.exceptions.ExceptionHandler;
import ru.krista.yargu.epishin.exceptions.tree.DBException;
import ru.krista.yargu.epishin.exceptions.tree.SaveDBTreeException;
import ru.krista.yargu.epishin.web.tree.TreeService;
import ru.krista.yargu.epishin.web.utils.Constants;

import java.io.IOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.krista.yargu.epishin.web.utils.StorageSystem;

public class NodeStorage{
    private Node tree = null;
    private TreeService treeService = null;

    private final Logger logger = LogManager.getLogger();

    public NodeStorage(){
        if (Constants.getStorageSystem() == StorageSystem.SAVE_TO_FILE) {
            logger.info("Попытка считывания дерева с файла");
            initByFile();
        }
        else {
            logger.info("Попытка считывания дерева с БД");
            initByDB();
        }
    }

    public void initByFile(){
        try {
            tree = Node.readJsonFileTree(Constants.TREE_FILE_PATH);
            logger.info("Дерево успешно считано с файла {}",Constants.TREE_FILE_PATH);
        } catch (IOException e) {
            logger.warn("Ошибка чтения файла дерева: {}. Причина: {}. Будет создано новое дерево",
                    Constants.TREE_FILE_PATH, e.getMessage());
            initBase();
        }
    }

    public void initBase(){
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

    public void initByDB() {
        try {
            treeService = new TreeService();
            tree = treeService.getTree();
            logger.info("Дерево успешно загружено из БД");
        }catch (DBException e) {
            ExceptionHandler.handleDBException(e,this);
        }
    }

    public void save(){
        if (Constants.getStorageSystem() == StorageSystem.SAVE_TO_FILE) {
            logger.info("Попытка сохранения дерева в файл");
            saveToFile();
        }
        else {
            logger.info("Попытка сохранения дерева с БД");
            saveToDB();
        }
    }

    private void saveToFile(){
        try {
            tree.writeJsonFileTree(Constants.TREE_FILE_PATH);
            logger.info("Дерево успешно сохранено в файл {}",Constants.TREE_FILE_PATH);
        } catch (IOException e) {
            logger.warn("Не удалось сохранить дерево в файл {}",Constants.TREE_FILE_PATH);
        }
    }
    private void saveToDB(){
        try {
            treeService.save();
            logger.info("Дерево успешно сохранено в БД");
        } catch (SaveDBTreeException e) {
            logger.warn("Не удалось сохранить дерево в БД");
        }
    }

    public Node getTree(){
        return tree;
    }
    public void setTreeService(TreeService service){
        this.treeService = service;
    }

}
