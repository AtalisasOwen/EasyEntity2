package com.atalisas.parser;

import com.atalisas.entity.FieldEntity;
import com.atalisas.entity.MappingAnnotationEntity;
import com.atalisas.utils.AnnotationUtils;
import com.atalisas.utils.ClassUtils;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;

public class DtoEntityFieldParser {

    public FieldEntity parse(Element innerElement, AnnotationMirror field) {
        FieldEntity fieldEntity = new FieldEntity();
        AnnotationValue name = AnnotationUtils.getAnnotationValue(field, "rename");
        fieldEntity.setName(name.getValue().toString());

        String packageName = ClassUtils.getPackage(innerElement.asType().toString()) + ".dto";
        String className = ClassUtils.getSimpleClassName(innerElement.asType().toString()) + "Dto";
        // parse Collection Entity -> Dto
        String totalName = packageName + "." + className;
        if (totalName.contains("<")){
            String[] s1 = totalName.split(">");
            totalName = s1[0] + "Dto>";
        }
        fieldEntity.setType(totalName);
        return fieldEntity;
    }

    public MappingAnnotationEntity generateMappingAnnotation(Element innerElement, AnnotationMirror field) {
        MappingAnnotationEntity annotationEntity = new MappingAnnotationEntity();
        AnnotationValue name = AnnotationUtils.getAnnotationValue(field, "rename");

        annotationEntity.setTarget(name.getValue().toString());
        annotationEntity.setSource(innerElement.toString());

        return annotationEntity;
    }

}
