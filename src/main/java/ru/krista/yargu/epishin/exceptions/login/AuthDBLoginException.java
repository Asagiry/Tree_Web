package ru.krista.yargu.epishin.exceptions.login;

public class AuthDBLoginException extends Exception{
    public AuthDBLoginException(String message){
        super(message);
    }
    public AuthDBLoginException(String message,Throwable cause){
        super(message,cause);
    }
}
