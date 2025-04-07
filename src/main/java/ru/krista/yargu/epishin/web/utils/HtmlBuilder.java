package ru.krista.yargu.epishin.web.utils;

public class HtmlBuilder {
    private HtmlBuilder(){
        throw new IllegalStateException("Utility class");
    }
    public static String buildStart(String title){
        return
                "<html>"+
                "  <head>"+
                "    <title>" + title+ "</title>"+
                "  </head>"+
                "  <body>";
    }
    public static String buildEnd(){
        return
                "  </body>"+
                "</html>";
    }

    public static String buildFormPost(String path,String params,String value){
        return  "    <form method=\"post\" action=\"" + path +"/"+ params + "\">"+
                "      <input type=\"submit\" value=\""+value+"\"/>"+
                "    </form>";


    }
}
