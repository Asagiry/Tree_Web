package ru.krista.yargu.epishin.exceptions.tree;

public class InitDBTreeException extends Exception {
    public InitDBTreeException(String message){
        super(message);
    }
    public InitDBTreeException(String message, Throwable cause){
        super(message,cause);
    }
}
