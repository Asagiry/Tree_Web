package ru.krista.yargu.epishin.web.utils;

public final class Constants {
    //mvn sonar:sonar -Dsonar.host.url=https://sonar.stud.krista.ru/ -Dsonar.token=squ _97ec783f998555f0a7dc09 ffb203c6f11e837180
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
    private static StorageSystem storageSystem = StorageSystem.SAVE_TO_DB;
    public static void setStorageSystem(StorageSystem system){
        storageSystem = system;
    }
    public static StorageSystem getStorageSystem(){return storageSystem;}
    //LOGIN
    public static final String LOGIN_BASE_PATH = SERVER_BASE_PATH+"login";
    public static final String LOGIN_SUCCESS_PATH = LOGIN_BASE_PATH+"/success";
    public static final String LOGIN_FAILURE_PATH = LOGIN_BASE_PATH+"/failure";
    //LIST

    //Exceptions
    public static final String CLEAR_DB_TREE_EX_MESSAGE = "Не удалось очистить БД";
    public static final String EMPTY_DB_TREE_EX_MESSAGE = "БД не содержит ни одной записи";
    public static final String INIT_DB_TREE_EX_MESSAGE = "Не удалось инициализировать БД";
    public static final String LOAD_DB_TREE_EX_MESSAGE = "Не удалось загрузить дерево из БД";
    public static final String SAVE_DB_TREE_EX_MESSAGE = "Не удалось сохранить дерево в БД";



}
