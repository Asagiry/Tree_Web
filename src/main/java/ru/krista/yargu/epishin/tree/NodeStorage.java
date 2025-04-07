package ru.krista.yargu.epishin.tree;

import ru.krista.yargu.epishin.exceptions.tree.EmptyDBTreeException;
import ru.krista.yargu.epishin.exceptions.tree.InitDBTreeException;
import ru.krista.yargu.epishin.exceptions.tree.LoadDBTreeException;
import ru.krista.yargu.epishin.exceptions.tree.SaveDBTreeException;
import ru.krista.yargu.epishin.web.tree.TreeServise;
import ru.krista.yargu.epishin.web.utils.Constants;

import java.io.IOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NodeStorage{
    private Node tree = null;
    private TreeServise treeServise = null;

    private final Logger logger = LogManager.getLogger();

    public NodeStorage(){
        if (Constants.SAVE_TO_FILE) {
            logger.info("Попытка считывания дерева с файла");
            initByFile();
        }
        else {
            logger.info("Попытка считывания дерева с БД");
            initByDB();
        }
    }

    private void initByFile(){
        try {
            tree = Node.readJsonFileTree(Constants.TREE_FILE_PATH);
            logger.info("Дерево успешно считано с файла {}",Constants.TREE_FILE_PATH);
        } catch (IOException e) {
            logger.warn("Ошибка чтения файла дерева: {}. Причина: {}. Будет создано новое дерево",
                    Constants.TREE_FILE_PATH, e.getMessage());
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

    private void initByDB() {
        try {
            treeServise = new TreeServise();
            tree = treeServise.getTree();
            logger.info("Дерево успешно загружено из БД");

        } catch (EmptyDBTreeException e) {
            logger.warn("БД пуста. Создаем новое дерево");
            initBase();

        } catch (LoadDBTreeException e) {
            logger.warn("Ошибка загрузки из БД: {}. Создаем новое дерево", e.getMessage());
            logger.debug("Детали:", e);
            initBase();

        } catch (InitDBTreeException e) {
            logger.error("Сбой БД ({}). Переключаемся на файловое хранение", e.getMessage());
            initByFile();
        }
    }

    public void save(){
        if (Constants.SAVE_TO_FILE) {
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
            treeServise.save();
            logger.info("Дерево успешно сохранено в БД");
        } catch (SaveDBTreeException e) {
            logger.warn("Не удалось сохранить дерево в БД");
        }
    }

    public Node getTree(){
        return tree;
    }

}
