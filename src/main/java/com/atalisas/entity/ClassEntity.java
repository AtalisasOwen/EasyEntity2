package com.atalisas.entity;

import java.util.List;

public class ClassEntity {
    String packageName;
    String className;
    List<FieldEntity> fieldEntities;
    List<MethodEntity> methodEntities;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("package " + packageName + ";\n\n");
        sb.append("public class " + className + " {\n");
        for (FieldEntity field : this.fieldEntities) {
            sb.append("    private " + field.toString() + ";\n");
        }
        sb.append("\n");
        for (MethodEntity method : this.methodEntities) {
            sb.append(method.toString() + "\n");
        }
        sb.append("}");
        return sb.toString();
    }

    public void addToStringMethod() {
        MethodEntity toStringEntity = new MethodEntity();
        toStringEntity.setReturnType("String");
        toStringEntity.setName("toString");

        StringBuilder sb = new StringBuilder();

        sb.append("return \"" + this.className + "{\" +\n");

        for (FieldEntity e : this.fieldEntities){
            sb.append("                \", " + e.getName() + "=\" + " + e.getName() + " +\n");
        }
        sb.append("                '}';");
        String innerCode = sb.toString();
        System.out.println(innerCode);
        innerCode = innerCode.replaceFirst(", ", "");
        System.out.println(innerCode);
        toStringEntity.setInnerCode(innerCode);
        this.methodEntities.add(toStringEntity);
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

    public List<FieldEntity> getFieldEntities() {
        return fieldEntities;
    }

    public void setFieldEntities(List<FieldEntity> fieldEntities) {
        this.fieldEntities = fieldEntities;
    }

    public List<MethodEntity> getMethodEntities() {
        return methodEntities;
    }

    public void setMethodEntities(List<MethodEntity> methodEntities) {
        this.methodEntities = methodEntities;
    }
}
