package ru.krista.yargu.epishin.exceptions.tree;


import ru.krista.yargu.epishin.web.utils.Constants;

public class LoadDBTreeException extends DBException{
    public LoadDBTreeException(String message){
        super(message);
    }
    public LoadDBTreeException(Throwable cause){
        super(Constants.LOAD_DB_TREE_EX_MESSAGE,cause);
    }
    public LoadDBTreeException(String message, Throwable cause){
        super(message,cause);
    }
}
