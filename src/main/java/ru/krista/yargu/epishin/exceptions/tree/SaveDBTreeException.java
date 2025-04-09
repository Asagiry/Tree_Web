package ru.krista.yargu.epishin.exceptions.tree;

import ru.krista.yargu.epishin.web.utils.Constants;

public class SaveDBTreeException extends DBException{
    public SaveDBTreeException(String message) {
        super(message);
    }
    public SaveDBTreeException(Throwable cause){
        super(Constants.SAVE_DB_TREE_EX_MESSAGE,cause);
    }
    public SaveDBTreeException(String message,Throwable cause){
        super(message, cause);
    }
}
