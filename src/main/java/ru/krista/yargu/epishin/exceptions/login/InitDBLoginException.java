package ru.krista.yargu.epishin.exceptions.login;

public class InitDBLoginException extends Exception{
    public InitDBLoginException(String message){
        super(message);
    }
    public InitDBLoginException(String message,Throwable cause){
        super(message, cause);
    }
}
