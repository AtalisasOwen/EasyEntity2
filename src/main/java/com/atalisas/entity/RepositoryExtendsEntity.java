package com.atalisas.entity;

import java.util.ArrayList;
import java.util.List;

public class RepositoryExtendsEntity {
    String className;
    List<String> genericTypes;

    @Override
    public String toString() {
        String genericString = String.join(", ", genericTypes);
        return className + "<" + genericString + ">";
    }

    public void addGenericType(String type){
        if (this.genericTypes == null){
            this.genericTypes = new ArrayList<>();
            this.genericTypes.add(type);
        } else {
            this.genericTypes.add(type);
        }
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public List<String> getGenericTypes() {
        return genericTypes;
    }

    public void setGenericTypes(List<String> genericTypes) {
        this.genericTypes = genericTypes;
    }
}
