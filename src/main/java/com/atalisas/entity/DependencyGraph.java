package com.atalisas.entity;

import com.atalisas.utils.ClassUtils;

import javax.sql.rowset.serial.SerialStruct;
import java.util.*;
import java.util.stream.Collectors;

public class DependencyGraph {
    public static Map<String, Set<String>> GRAPH = new HashMap<>();
    public static Map<String, Set<String>> USER_INPUT_GRAPH = new HashMap<>();
    public static Map<String, String> DTO = new HashMap<>();
    public static Map<String, String> MAPPER = new HashMap<>();

    public static void addMapper(String entity, String mapper){
        System.out.println("Mapper ADDDDD: "+entity + " => "+mapper);
        MAPPER.put(entity, mapper);
    }

    public static String getMapper(String service){
        String[] ss = service.split("\\.");
        String serviceName = ss[ss.length-1].replaceFirst("Service", "");
        System.out.println("SERVICE: " + service);
        System.out.println("Mapper: " + MAPPER);
        for (Map.Entry<String, String> entry : MAPPER.entrySet()){
            if (entry.getKey().contains(serviceName)){
                return entry.getValue();
            }
        }
        return null;
    }

    public static void addDto(String entity, String dto){
        System.out.println("ADD ENTITY: " + entity + " => " + dto);
        DTO.put(entity, dto);
        DTO.put("java.util.List<" + entity + ">", "java.util.List<" + dto +">");
        DTO.put("java.util.Set<" + entity + ">", "java.util.Set<" + dto + ">");
    }

    public static String getDto(String entity){
        return DTO.getOrDefault(entity, entity);
    }


    public static void addUserInputDependency(String parent, String dependency){
        System.out.println("User Input: " + parent + "--" + dependency);
        if (USER_INPUT_GRAPH.containsKey(parent)){
            USER_INPUT_GRAPH.get(parent).add(dependency);
        }else {
            Set<String> list = new HashSet<>();
            list.add(dependency);
            USER_INPUT_GRAPH.put(parent, list);
        }
    }

    public static void addDependency(String parent, String dependency){
        System.out.println("Graph Input: " + parent + "--" + dependency);
        if (GRAPH.containsKey(parent)){
            GRAPH.get(parent).add(dependency);
        }else {
            Set<String> list = new HashSet<>();
            list.add(dependency);
            GRAPH.put(parent, list);
        }
    }

    public static String getUsesClassString(String mapperPackageName, String mapperClassName){
        String s1 = getInputClassString(mapperPackageName, mapperClassName);
        String s2 = getUserClassesString(mapperPackageName, mapperClassName);
        System.out.println("GET S1: " + s1);
        System.out.println("GET S2: " + s2);
        if (s1 == null){
            return s2;
        } else if (s2 == null){
            return s1;
        } else {
            return s1 + ", " + s2;
        }
    }

    private static String getInputClassString(String mapperPackageName, String mapperClassName){
        System.out.println("PACKAGE: "+mapperPackageName);
        System.out.println("CLASS: " + mapperClassName);
        String orginClass = getOriginClass(mapperPackageName, mapperClassName);
        System.out.println("KEY: " + orginClass);
        Set<String> dependency = USER_INPUT_GRAPH.get(orginClass);
        if (dependency == null){
            return null;
        } else {
            System.out.println("DEPENDENCY: " + dependency);
            return String.join(", ", dependency);
        }

    }

    private static String getUserClassesString(String mapperPackageName, String mapperClassName){
        System.out.println("PACKAGE: "+mapperPackageName);
        System.out.println("CLASS: " + mapperClassName);
        String orginClass = getOriginClass(mapperPackageName, mapperClassName);
        System.out.println("KEY: " + orginClass);

        Set<String> dependency = GRAPH.get(orginClass);
        if (dependency == null){
            return null;
        } else {
            System.out.println("DEPENDENCY: " + dependency);
            List<String> mappers = dependency.stream()
                    .map(DependencyGraph::getMapperClass)
                    .map(s -> s+".class")
                    .collect(Collectors.toList());
            return String.join(", ", mappers);
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
