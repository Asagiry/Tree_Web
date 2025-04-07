package ru.krista.yargu.epishin.exceptions.tree;

public class ClearDBTreeException extends Exception{
    public ClearDBTreeException(String message){
        super(message);
    }
    public ClearDBTreeException(String message, Throwable cause){
        super(message,cause);
    }
}
