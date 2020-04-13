package com.atalisas.parser;

import com.atalisas.entity.ControllerClassEntity;
import com.atalisas.entity.ControllerFieldEntity;
import com.atalisas.entity.ControllerMethodEntity;
import com.atalisas.entity.DependencyGraph;
import com.atalisas.utils.ElementUtils;

import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class AutoControllerParser {

    public String[] getMethodAnnotation(String methodName){
        String[] ss = new String[2];
        if (methodName.startsWith("get")){
            ss[0] = "@GetMapping";
            ss[1] = methodName.replaceFirst("get", "");
        } else if (methodName.startsWith("post")){
            ss[0] = "@PostMapping";
            ss[1] = methodName.replaceFirst("post", "");
        } else if (methodName.startsWith("add")){
            ss[0] = "@PostMapping";
            ss[1] = methodName.replaceFirst("add", "");
        } else if (methodName.startsWith("update")){
            ss[0] = "@PutMapping";
            ss[1] = methodName.replaceFirst("update", "");
        } else if (methodName.startsWith("motify")){
            ss[0] = "@PutMapping";
            ss[1] = methodName.replaceFirst("motify", "");
        } else if (methodName.startsWith("delete")){
            ss[0] = "@DeleteMapping";
            ss[1] = methodName.replaceFirst("delete", "");
        }else if (methodName.startsWith("remove")){
            ss[0] = "@DeleteMapping";
            ss[1] = methodName.replaceFirst("remove", "");
        } else {
            ss[0] = "@GetMapping";
            ss[1] = methodName;
        }
        return ss;
    }

    public String parseUrl(String methodName, List<? extends VariableElement> parameters){
        String url = "/"; //"Employee,Dprt,Team";
        String[] paths = methodName.split("By");
        if (paths.length == 1){

            if (parameters.size() > 0 && parameters.get(0).asType().toString().startsWith("java.lang")){
                System.out.println("TTTTTTTTTTTTTTTTTTTTTTTTTTTTTT");
                return url + paths[0].toLowerCase() + "/{" + parameters.get(0) + "}";
            }
            System.out.println("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF");
            return url + paths[0].toLowerCase();
        }else{
            url = url + paths[0].toLowerCase();
            for (int i = 1; i < paths.length; i++){
                url = url + "/" + paths[i].toLowerCase();
                if (parameters.get(i-1).asType().toString().startsWith("java.lang")){
                    url = url + "/" + "{" + parameters.get(i-1) +"}";
                }
            }
        }
        return url;
    }

    public String parseMethodToUrl(String methodName, List<? extends VariableElement> parameters){
        String[] methodWithAnno = getMethodAnnotation(methodName);
        String url = parseUrl(methodWithAnno[1], parameters);
        System.out.println("URL:" + url);
        String annotation = methodWithAnno[0] + "(\"" + url + "\")" ;
        return annotation;
    }

    public List<ControllerMethodEntity.Field> toParas(List<? extends VariableElement> parameters){
        List<ControllerMethodEntity.Field> fields = new ArrayList<>();
        for (VariableElement para : parameters){
            ControllerMethodEntity.Field cPara = new ControllerMethodEntity.Field();
            if (para.asType().toString().startsWith("java.lang")){
                cPara.setParaType(para.asType().toString());
                cPara.setParaName(para.toString());
                cPara.getAnnotation().add("@PathVariable(\"" + para.toString() + "\")");
                fields.add(cPara);
            } else if (para.asType().toString().equals("org.springframework.data.domain.Pageable")){
                ControllerMethodEntity.Field pagePara = new ControllerMethodEntity.Field();
                pagePara.setParaType("int");
                pagePara.setParaName("page");
                pagePara.getAnnotation().add("@RequestParam(\"page\")");
                ControllerMethodEntity.Field limitPara = new ControllerMethodEntity.Field();
                limitPara.setParaType("int");
                limitPara.setParaName("limit");
                pagePara.getAnnotation().add("@RequestParam(\"limit\")");
                fields.add(pagePara);
                fields.add(limitPara);
            } else {
                cPara.setParaType(para.asType().toString());
                cPara.setParaName(para.toString());
                cPara.getAnnotation().add("@Valid");
                cPara.getAnnotation().add("@RequestBody");
                fields.add(cPara);
            }
        }
        return fields;

    }

    public List<String> toThrows(List<? extends TypeMirror> throwsTypes){
        List<String> ss = new ArrayList<>();
        for (TypeMirror type : throwsTypes){
            ss.add(type.toString());
        }
        return ss;
    }

    public ControllerMethodEntity translateMethod(String methodName,
                                                  TypeMirror returnType,
                                                  List<? extends VariableElement> parameters,
                                                  List<? extends TypeMirror> throwsTypes){
        String annotation = parseMethodToUrl(methodName, parameters);
        System.out.println("ANNOTATION: " + annotation);

        ControllerMethodEntity entity = new ControllerMethodEntity();
        entity.setMethodName(methodName);
        entity.getAnnotations().add(annotation);
        entity.setReturnType(returnType.toString());
        entity.setMethodParas(toParas(parameters));
        entity.setThrowsStr(toThrows(throwsTypes));
        entity.setInnerCode("        return null;");

        return entity;
    }


    private String toControllerClass(String entityType){
        String[] ss = entityType.split("\\.");
        return ss[ss.length-1].replaceFirst("Service","")+"Controller";
    }

    private String toControllerPackage(String entityType){
        String[] ss = entityType.split("\\.");
        if (ss.length <= 2){
            return "controller";
        }else{
            List<String> s = new ArrayList<>();
            for (int i = 0; i < ss.length - 2; i++){
                s.add(ss[i]);
            }
            s.add("controller");
            return String.join(".", s);
        }
    }


    public List<ControllerClassEntity> parse(Set<? extends Element> classes){
        List<ControllerClassEntity> controllerClassEntities = new ArrayList<>();
        for (Element s : classes) {
            System.out.println("Entity Class: " + s.asType().toString());
            AnnotationMirror serviceEntity = ElementUtils.judgeFieldWithAnnotion(s, "AutoController");
            System.out.println("AnnotationMirror: " + serviceEntity);

            ControllerClassEntity controllerClassEntity = new ControllerClassEntity();
            controllerClassEntity.getAnnotations().add("@RestController");
            controllerClassEntity.getAnnotations().add("@RequestMapping(\"/api\")");
            controllerClassEntity.setClassName(toControllerClass(s.asType().toString()));
            controllerClassEntity.setPackageName(toControllerPackage(s.asType().toString()));
            controllerClassEntity.getImports().add("import javax.validation.*;");
            controllerClassEntity.getImports().add("import java.util.stream.Collectors;");
            controllerClassEntity.getImports().add("import org.springframework.web.bind.annotation.*;");
            controllerClassEntity.getImports().add("import org.springframework.beans.factory.annotation.Autowired;");
            controllerClassEntity.getImports().add("import org.springframework.data.domain.PageRequest;");

            ControllerFieldEntity fieldEntity = new ControllerFieldEntity();
            fieldEntity.getAnnotations().add("@Autowired");
            fieldEntity.setFieldName("service");
            fieldEntity.setTypeName(s.asType().toString());
            controllerClassEntity.getFieldEntities().add(fieldEntity);

            String mapperClass = DependencyGraph.getMapper(s.asType().toString());
            if (mapperClass != null){
                ControllerFieldEntity mapperEntity = new ControllerFieldEntity();
                mapperEntity.setFieldName("mapper = " + mapperClass + ".INSTANCE");
                mapperEntity.setTypeName(mapperClass);
                controllerClassEntity.getFieldEntities().add(mapperEntity);
            }



            for (Element innerElement : s.getEnclosedElements()) {
                if (innerElement.getKind().equals(ElementKind.METHOD) && innerElement.getModifiers().contains(Modifier.PUBLIC)) {
                    ExecutableElement methodSign = (ExecutableElement) innerElement;
                    System.out.println("METHOD SIGN NAME: " + methodSign.getSimpleName());
                    System.out.println("METHOD SIGN RETURN: " + methodSign.getReturnType());
                    System.out.println("METHOD SIGN Parameters: " + methodSign.getParameters() + ""
                            + methodSign.getParameters().stream().map(VariableElement::asType).collect(Collectors.toList()));
                    System.out.println("METHOD SIGN Thrown: " + methodSign.getThrownTypes());
                    System.out.println("+++++++++++++++++++++++++++++++++++++++++++++");
                    ControllerMethodEntity entity = translateMethod(methodSign.getSimpleName().toString(),
                            methodSign.getReturnType(),
                            methodSign.getParameters(),
                            methodSign.getThrownTypes());
                    controllerClassEntity.getMethodEntities().add(entity);
                    System.out.println(entity);
                }
            }
            controllerClassEntities.add(controllerClassEntity);
        }
        return controllerClassEntities;
    }

}
