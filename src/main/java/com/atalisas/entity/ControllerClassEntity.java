package com.atalisas.entity;

import java.util.ArrayList;
import java.util.List;

public class ControllerClassEntity {

    String packageName = "";
    String className = "";

    List<String> imports = new ArrayList<>();
    List<String> annotations = new ArrayList<>();
    List<ControllerFieldEntity> fieldEntities = new ArrayList<>();
    List<ControllerMethodEntity> methodEntities = new ArrayList<>();

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("package ");
        sb.append(packageName);
        sb.append(";\n\n");
        imports.stream().forEach(s -> {
            sb.append(s).append("\n");
        });
        sb.append("\n");
        annotations.stream().forEach(s -> {
            sb.append(s).append("\n");
        });
        sb.append("public class ");
        sb.append(className);
        sb.append(" {\n");
        fieldEntities.stream().forEach(s -> {
            sb.append(s.toString()).append("\n");
        });
        methodEntities.stream().forEach(s -> {
            sb.append(s.toString()).append("\n");
        });
        sb.append("}\n");
        return sb.toString();
    }

    public List<ControllerFieldEntity> getFieldEntities() {
        return fieldEntities;
    }

    public void setFieldEntities(List<ControllerFieldEntity> fieldEntities) {
        this.fieldEntities = fieldEntities;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public List<String> getImports() {
        return imports;
    }

    public void setImports(List<String> imports) {
        this.imports = imports;
    }

    public List<String> getAnnotations() {
        return annotations;
    }

    public void setAnnotations(List<String> annotations) {
        this.annotations = annotations;
    }

    public List<ControllerMethodEntity> getMethodEntities() {
        return methodEntities;
    }

    public void setMethodEntities(List<ControllerMethodEntity> methodEntities) {
        this.methodEntities = methodEntities;
    }
}
