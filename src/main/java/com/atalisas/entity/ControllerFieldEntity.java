package com.atalisas.entity;

import java.util.ArrayList;
import java.util.List;

public class ControllerFieldEntity {
    List<String> annotations = new ArrayList<>();
    String typeName = "";
    String fieldName = "";

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        annotations.stream().forEach(a -> {
            sb.append("    ").append(a).append("\n");
        });
        sb.append("    "+typeName + " " + fieldName +";\n");
        return sb.toString();
    }

    public List<String> getAnnotations() {
        return annotations;
    }

    public void setAnnotations(List<String> annotations) {
        this.annotations = annotations;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }
}
