package com.atalisas.entity;

import java.util.List;
import java.util.stream.Collectors;

public class MethodEntity {

    String returnType;
    List<FieldEntity> paraTypes;
    String name;
    String innerCode;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("    public " + returnType + " " + name + "(");
        if (paraTypes != null){
            sb.append("" + String.join(", ", paraTypes.stream().map(FieldEntity::toString).collect(Collectors.toList())));
        }
        sb.append(") {\n");
        sb.append("    "+innerCode);
        sb.append("\n");
        sb.append("    }\n");
        return sb.toString();
    }

    public String getInnerCode() {
        return innerCode;
    }

    public void setInnerCode(String innerCode) {
        this.innerCode = innerCode;
    }

    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    public List<FieldEntity> getParaTypes() {
        return paraTypes;
    }

    public void setParaTypes(List<FieldEntity> paraTypes) {
        this.paraTypes = paraTypes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
