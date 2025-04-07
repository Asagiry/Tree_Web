package ru.krista.yargu.epishin.web.utils;

public final class Constants {
    private Constants(){
        throw new IllegalStateException("Utility class");
    }
    //WEBSERVER
    public static final int SERVER_PORT = 8081;
    public static final String SERVER_HOST = "0.0.0.0";
    public static final String SERVER_URL = "http://localhost:"+SERVER_PORT;

    public static final String SERVER_BASE_PATH = "/";

    //REST_APPLICATION
    public static final String TREE_FILE_PATH = "ServerTree";

    //CONTROLLERS

    public static final String URI_SYNTAX_ERROR_MESSAGE = "Ошибка построения URI для перенаправления";

    //TREE_PRESENTATION_CONTROLLER
    public static final String TREE_BASE_PATH = SERVER_BASE_PATH+"tree";
    public static final String TREE_ADD_PATH = TREE_BASE_PATH+"/add";
    public static final String TREE_EDIT_PATH = TREE_BASE_PATH+"/edit";
    public static final String TREE_DELETE_PATH = TREE_BASE_PATH+"/delete";
    //NODE_STORAGE
    public static final boolean SAVE_TO_FILE = Boolean.TRUE;

    //LOGIN
    public static final String LOGIN_BASE_PATH = SERVER_BASE_PATH+"login";
    public static final String LOGIN_SUCCESS_PATH = LOGIN_BASE_PATH+"/success";
    public static final String LOGIN_FAILURE_PATH = LOGIN_BASE_PATH+"/failure";

    //LIST
    //path consts



}
