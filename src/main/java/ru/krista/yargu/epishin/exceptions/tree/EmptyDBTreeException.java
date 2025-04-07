package ru.krista.yargu.epishin.exceptions.tree;

public class EmptyDBTreeException extends Exception{
    public EmptyDBTreeException(String message){
        super(message);
    }
    public EmptyDBTreeException(String message, Throwable cause){
        super(message,cause);
    }
}
