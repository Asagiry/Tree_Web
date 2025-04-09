package ru.krista.yargu.epishin.exceptions;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.krista.yargu.epishin.exceptions.tree.ClearDBTreeException;
import ru.krista.yargu.epishin.exceptions.tree.DBException;
import ru.krista.yargu.epishin.exceptions.tree.EmptyDBTreeException;
import ru.krista.yargu.epishin.exceptions.tree.InitDBTreeException;
import ru.krista.yargu.epishin.exceptions.tree.LoadDBTreeException;
import ru.krista.yargu.epishin.exceptions.tree.SaveDBTreeException;
import ru.krista.yargu.epishin.tree.NodeStorage;
import ru.krista.yargu.epishin.web.tree.TreeService;
import ru.krista.yargu.epishin.web.utils.Constants;
import ru.krista.yargu.epishin.web.utils.StorageSystem;


public final class ExceptionHandler {
    private static final Logger logger = LogManager.getLogger();
    private ExceptionHandler(){
        throw new IllegalStateException("Utility class");
    }

    public static void handleDBException(DBException exception, NodeStorage storage){
       if (exception instanceof ClearDBTreeException){
           handleAndChangeFileSystem(exception,storage);
       }
       else if (exception instanceof EmptyDBTreeException){
           handleEmptyDBTreeException(exception,storage);
       }
       else if (exception instanceof InitDBTreeException){
           handleAndChangeFileSystem(exception,storage);
       }
       else if (exception instanceof LoadDBTreeException){
           handleAndChangeFileSystem(exception,storage);
       }
       else if (exception instanceof SaveDBTreeException){
           handleAndChangeFileSystem(exception,storage);
       }
    }
    private static void handleEmptyDBTreeException(DBException exception,NodeStorage storage) {
        logger.warn("{}. Будет создано новое дерево",exception.getMessage());
        logger.debug(exception.getCause());
        storage.initBase();
        try {
            storage.setTreeService(new TreeService(storage.getTree()));
            storage.save();
        } catch (SaveDBTreeException e) {
            handleAndChangeFileSystem(exception,storage);
        }
    }
    private static void handleAndChangeFileSystem(DBException exception,NodeStorage storage){
        logger.error("{}. Будет использована файловая система",exception.getMessage());
        logger.debug(exception.getCause());
        Constants.setStorageSystem(StorageSystem.SAVE_TO_FILE);
        storage.initByFile();
    }



}
