package com.atalisas.utils;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.ExecutableElement;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AnnotationUtils {

    public static AnnotationValue getAnnotationValue(AnnotationMirror annotationMirror, String methodName){
        Map<? extends ExecutableElement, ? extends AnnotationValue> annVal =  annotationMirror.getElementValues();   // 注解内容
        System.out.println("ANNVAL: " + annVal);
        List<AnnotationValue> values = annVal.entrySet().stream()
                .filter(entry -> entry.getKey().getSimpleName().contentEquals(methodName))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
        if (values.size() > 0){
            return values.get(0);
        }else {
            return null;
        }
    }
}
