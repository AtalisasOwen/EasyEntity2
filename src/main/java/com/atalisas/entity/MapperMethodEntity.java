package com.atalisas.entity;


import java.util.ArrayList;
import java.util.List;

public class MapperMethodEntity {
    String returnType;
    String paraType;
    String methodName;
    List<MappingAnnotationEntity> mappingAnnotationEntityList;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (mappingAnnotationEntityList != null){
            mappingAnnotationEntityList.stream().forEach(sb::append);
        }
        sb.append("    "+returnType + " " + methodName + "(" + paraType +" " + "e);\n");
        return sb.toString();
    }

    public void addMappingAnnotationEntity(MappingAnnotationEntity e){
        if (this.mappingAnnotationEntityList == null){
            this.mappingAnnotationEntityList = new ArrayList<>();
        }
        this.mappingAnnotationEntityList.add(e);
    }

    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    public String getParaType() {
        return paraType;
    }

    public void setParaType(String paraType) {
        this.paraType = paraType;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public List<MappingAnnotationEntity> getMappingAnnotationEntityList() {
        return mappingAnnotationEntityList;
    }

    public void setMappingAnnotationEntityList(List<MappingAnnotationEntity> mappingAnnotationEntityList) {
        this.mappingAnnotationEntityList = mappingAnnotationEntityList;
    }
}
