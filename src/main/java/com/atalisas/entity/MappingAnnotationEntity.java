package com.atalisas.entity;

import java.util.ArrayList;
import java.util.List;

public class MappingAnnotationEntity {
    String target;
    String source;
    String expression;

    List<String> importClasses;


    public void addImportClass(String importClass){
        if (importClasses == null){
            this.importClasses = new ArrayList<>();
        }
        this.importClasses.add(importClass);
    }

    public List<String> getImportClass() {
        return importClasses;
    }

    public void setImportClass(List<String> importClasses) {
        this.importClasses = importClasses;
    }

    @Override
    public String toString() {
        if (source == null){
            return "    @Mapping(target = \"" + target + "\", expression = \"" + expression +"\")\n";
        } else if (expression == null){
            return "    @Mapping(source = \"" + source + "\", target = \"" + target + "\")\n";
        }
        return "";
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }
}
