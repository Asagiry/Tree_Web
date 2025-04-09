package ru.krista.yargu.epishin.exceptions.tree;

import ru.krista.yargu.epishin.web.utils.Constants;

public class ClearDBTreeException extends DBException{

    public ClearDBTreeException(String message){
        super(message);
    }
    public ClearDBTreeException(Throwable cause){
        super(Constants.CLEAR_DB_TREE_EX_MESSAGE,cause);
    }
    public ClearDBTreeException(String message, Throwable cause){
        super(message,cause);
    }
}
