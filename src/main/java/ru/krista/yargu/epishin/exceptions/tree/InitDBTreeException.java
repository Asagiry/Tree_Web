package ru.krista.yargu.epishin.exceptions.tree;

import ru.krista.yargu.epishin.web.utils.Constants;

public class InitDBTreeException extends DBException{

    public InitDBTreeException(String message){
        super(message);
    }
    public InitDBTreeException(Throwable cause){
        super(Constants.INIT_DB_TREE_EX_MESSAGE,cause);
    }
    public InitDBTreeException(String message, Throwable cause){
        super(message,cause);
    }
}
