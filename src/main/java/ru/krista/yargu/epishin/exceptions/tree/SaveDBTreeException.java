package ru.krista.yargu.epishin.exceptions.tree;

public class SaveDBTreeException extends Exception{
    public SaveDBTreeException(String message) {
        super(message);
    }
    public SaveDBTreeException(String message,Throwable cause){
        super(message, cause);
    }
}
