package com.atalisas.entity;

public class FieldEntity {
    String type;
    String name;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        if (type.equals("void")){
            return "";
        }
        return type + " " + name;
    }

    public MethodEntity toSetter(){
        return null;
    }

    public MethodEntity toGetter(){
        return null;
    }


}
