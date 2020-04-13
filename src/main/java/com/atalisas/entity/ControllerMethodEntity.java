package com.atalisas.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ControllerMethodEntity {


    public static class Field{
        List<String> annotation = new ArrayList<>();
        String paraName = "";
        String paraType = "";

        public List<String> getAnnotation() {
            return annotation;
        }

        public void setAnnotation(List<String> annotation) {
            this.annotation = annotation;
        }

        public String getParaName() {
            return paraName;
        }

        public void setParaName(String paraName) {
            this.paraName = paraName;
        }

        public String getParaType() {
            return paraType;
        }

        public void setParaType(String paraType) {
            this.paraType = paraType;
        }

        public String toString(){
            String anns = String.join(" ", annotation);
            return anns + " " + DependencyGraph.getDto(paraType) + " " + paraName;
        }
    }

    String methodName = "";
    String returnType = "";
    List<String> annotations = new ArrayList<>();
    List<Field> methodParas = new ArrayList<>();
    List<String> throwsStr = new ArrayList<>();
    String innerCode = "";

    public String toString(){
        StringBuilder sb = new StringBuilder();
        annotations.stream().forEach(s -> {
            sb.append("    ").append(s).append("\n");
        });
        sb.append("    public ");
        sb.append(DependencyGraph.getDto(returnType));
        sb.append(" ");
        sb.append(methodName);
        sb.append("(");
        String paras = String.join(", ", methodParas.stream().map(Field::toString).collect(Collectors.toList()));
        sb.append(paras);
        sb.append(") ");
        if (throwsStr.size() > 0){
            sb.append("throws ");
            String throwStr = String.join(", ", throwsStr);
            sb.append(throwStr);
        }
        sb.append(" {\n");
        sb.append(generateInnerCode(!DependencyGraph.getDto(returnType).equals(returnType)));
        sb.append("\n");
        sb.append("    }\n");
        return sb.toString();
    }

    public String generateInnerCode(boolean dto){
        StringBuilder sb = new StringBuilder();
        sb.append("        return ");
        if (!dto){
            sb.append("service."+methodName+"(");
            String paras = this.methodParas.stream().map(Field::getParaName).collect(Collectors.joining(", "));
            sb.append(paras);
            sb.append(");");
        }else{
            if (this.returnType.startsWith("java.util.List")){
                sb.append("service."+methodName+"(");
                String paras = this.methodParas.stream().map(Field::getParaName).collect(Collectors.joining(", "));
                sb.append(paras);
                sb.append(").stream().map(mapper::toDto).collect(Collectors.toList());");
            } else if(this.returnType.startsWith("java.util.Set")){
                sb.append("service."+methodName+"(");
                String paras = this.methodParas.stream().map(Field::getParaName).collect(Collectors.joining(", "));
                sb.append(paras);
                sb.append(").stream().map(mapper::toDto).collect(Collectors.toSet());");
            }else{
                sb.append("mapper.toDto(service."+methodName+"(");
                String paras = this.methodParas.stream().map(Field::getParaName).collect(Collectors.joining(", "));
                sb.append(paras);
                sb.append("));");
            }

        }
        System.out.println("XXXXXXXXXXXXXX: " + sb.toString());
        return sb.toString();
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    public List<String> getAnnotations() {
        return annotations;
    }

    public void setAnnotations(List<String> annotations) {
        this.annotations = annotations;
    }

    public List<Field> getMethodParas() {
        return methodParas;
    }

    public void setMethodParas(List<Field> methodParas) {
        this.methodParas = methodParas;
    }

    public List<String> getThrowsStr() {
        return throwsStr;
    }

    public void setThrowsStr(List<String> throwsStr) {
        this.throwsStr = throwsStr;
    }

    public String getInnerCode() {
        return innerCode;
    }

    public void setInnerCode(String innerCode) {
        this.innerCode = innerCode;
    }
}
