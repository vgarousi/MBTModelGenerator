package com.sasi.gwmaker.util;

public class StringUtil {

    public static String processFileName(String fileName){
        return fileName.replaceAll("[^a-zA-Z0-9]", " ");
    }

    public static String processVertexName(String name){
        return "v_" + removeSpaces(name);
    }

    public static String processEdgeName(String name){
        return "e_" + removeSpaces(name);
    }

    public static String removeSpaces(String name){
        return name
                .replace(" ", "")
                .replace("\n", "")
                .replace("\r", "");
    }

}
