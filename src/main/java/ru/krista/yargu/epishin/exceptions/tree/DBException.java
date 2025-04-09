package ru.krista.yargu.epishin.exceptions.tree;

public abstract class DBException extends Exception {
    protected DBException(String message){
        super(message);
    }
    protected DBException(String message,Throwable cause){
        super(message, cause);
    }
}
