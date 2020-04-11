package com.atalisas.utils;


import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import java.util.List;
import java.util.stream.Collectors;

public class ElementUtils {


    public static Element getInnerFieldByName(Element element, String name){
        for (Element innerElementOfField : element.getEnclosedElements()) {
            if (innerElementOfField.getSimpleName().toString().contentEquals(name)) {
                return innerElementOfField;
            }
        }
        throw new IllegalArgumentException("DtoField出错");
    }

    public static List<Element> getInnerField(Element element){
        return element.getEnclosedElements()
                .stream()
                .filter(innerEle -> innerEle.getKind().isField())
                .collect(Collectors.toList());
    }

    public static List<Element> getInnerSetter(Element element){
        return element.getEnclosedElements()
                .stream()
                .filter(innerEle -> innerEle.getKind().equals(ElementKind.METHOD))
                .filter(innerEle -> innerEle.getSimpleName().toString().startsWith("set"))
                .collect(Collectors.toList());
    }

    public static List<Element> getInnerGetter(Element element){
        return element.getEnclosedElements()
                .stream()
                .filter(innerEle -> innerEle.getKind().equals(ElementKind.METHOD))
                .filter(innerEle -> innerEle.getSimpleName().toString().startsWith("get"))
                .collect(Collectors.toList());
    }

    public static AnnotationMirror judgeFieldWithAnnotion(Element element, String annotationName){
        List<? extends AnnotationMirror> anns = element.getAnnotationMirrors();
        anns =  anns.stream()
                .filter(a -> a.getAnnotationType().asElement().getSimpleName().contentEquals(annotationName))
                .collect(Collectors.toList());
        if (anns.size() > 0){
            return anns.get(0);
        }else {
            return null;
        }
    }

}
