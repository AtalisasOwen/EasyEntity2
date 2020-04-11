package com.atalisas.utils;

import com.atalisas.entity.FieldEntity;
import com.atalisas.entity.MethodEntity;

import java.util.Collections;

public class FieldMethodUtils {

    public static String captureName(String name) {
        char[] cs=name.toCharArray();
        cs[0]-=32;
        return String.valueOf(cs);
    }

    public static MethodEntity makeSetter(FieldEntity fieldEntity){
        MethodEntity method = new MethodEntity();
        String name = fieldEntity.getName();
        method.setName("set" + captureName(fieldEntity.getName()));
        method.setReturnType("void");
        method.setParaTypes(Collections.singletonList(fieldEntity));
        method.setInnerCode("    this." + name + " = " + name + ";");
        return method;
    }

    public static MethodEntity makeGetter(FieldEntity fieldEntity){
        MethodEntity method = new MethodEntity();
        String name = fieldEntity.getName();
        method.setName("get" + captureName(fieldEntity.getName()));
        method.setReturnType(fieldEntity.getType());
        method.setParaTypes(null);
        method.setInnerCode("    return this." + name + ";");
        return method;
    }

}
