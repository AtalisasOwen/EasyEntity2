package com.atalisas.utils;

import java.util.ArrayList;
import java.util.List;

public class ClassUtils {

    public static String getPackage(String totalName){
        StringBuilder sb = new StringBuilder();
        String[] packages = totalName.split("\\.");
        List<String> ss = new ArrayList<>();
        for (int i = 0; i < packages.length-1; i++){
            ss.add(packages[i]);
        }
        sb.append(String.join(".", ss));
        return sb.toString();
    }

    public static String getSimpleClassName(String totalName){
        String[] packages = totalName.split("\\.");
        return packages[packages.length-1];
    }
}
