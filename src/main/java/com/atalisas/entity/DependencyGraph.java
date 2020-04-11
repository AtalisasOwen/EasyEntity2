package com.atalisas.entity;

import com.atalisas.utils.ClassUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DependencyGraph {
    public static Map<String, List<String>> GRAPH = new HashMap<>();

    public static void addDependency(String parent, String dependency){
        System.out.println("Graph Input: " + parent + "--" + dependency);
        if (GRAPH.containsKey(parent)){
            GRAPH.get(parent).add(dependency);
        }else {
            List<String> list = new ArrayList<>();
            list.add(dependency);
            GRAPH.put(parent, list);
        }
    }

    public static String getUserClassesString(String mapperPackageName, String mapperClassName){
        System.out.println("PACKAGE: "+mapperPackageName);
        System.out.println("CLASS: " + mapperClassName);
        String orginClass = getOriginClass(mapperPackageName, mapperClassName);
        System.out.println("KEY: " + orginClass);

        List<String> dependency = GRAPH.get(orginClass);
        if (dependency == null){
            return null;
        } else {
            System.out.println("DEPENDENCY: " + dependency);
            List<String> mappers = dependency.stream()
                    .map(DependencyGraph::getMapperClass)
                    .collect(Collectors.toList());
            return String.join(", ", mappers)+".class";
        }
    }

    private static String getMapperClass(String originClass){
        String packageName = ClassUtils.getPackage(originClass)+".mapper";
        String className = ClassUtils.getSimpleClassName(originClass)+"Mapper";
        String totalName = packageName + "." + className;
        if (totalName.contains("<")){
            String[] s1 = totalName.split("<");
            String[] s2 = s1[1].split(">");
            totalName = s2[0] + "Mapper";
        }
        return totalName;
    }

    private static String getOriginClass(String mapperPackageName, String mapperClassName){
        String originPackage = ClassUtils.getPackage(mapperPackageName);
        String orginClass = mapperClassName.substring(0, mapperClassName.length()-6);
        return originPackage+"."+orginClass;
    }

}
