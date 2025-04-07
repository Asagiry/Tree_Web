package ru.krista.yargu.epishin.exceptions.tree;

public class LoadDBTreeException extends Exception{
    public LoadDBTreeException(String message){
        super(message);
    }
    public LoadDBTreeException(String message, Throwable cause){
        super(message,cause);
    }
}
