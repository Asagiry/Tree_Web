package ru.krista.yargu.epishin.exceptions.tree;

import ru.krista.yargu.epishin.web.utils.Constants;

public class EmptyDBTreeException extends DBException{
    public EmptyDBTreeException(){
        super(Constants.EMPTY_DB_TREE_EX_MESSAGE);
    }
}
