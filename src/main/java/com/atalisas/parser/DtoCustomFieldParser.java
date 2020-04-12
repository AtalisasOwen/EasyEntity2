package com.atalisas.parser;

import com.atalisas.entity.FieldEntity;
import com.atalisas.entity.MappingAnnotationEntity;
import com.atalisas.utils.AnnotationUtils;
import com.atalisas.utils.ClassUtils;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;

public class DtoCustomFieldParser {
    public FieldEntity parse(Element innerElement, AnnotationMirror field) {
        FieldEntity fieldEntity = new FieldEntity();
        AnnotationValue name = AnnotationUtils.getAnnotationValue(field, "rename");
        AnnotationValue typeCls = AnnotationUtils.getAnnotationValue(field, "typeClass");
        AnnotationValue mapperCls = AnnotationUtils.getAnnotationValue(field, "mapperClass");
        System.out.println("TypeClass: "+typeCls);
        System.out.println("MapperClass: " + mapperCls);

        fieldEntity.setName(name.getValue().toString());
        String typeClass = typeCls.getValue().toString();
        if (innerElement.asType().toString().contains("<")){
            String collectionType = innerElement.asType().toString().split("<")[0];
            typeClass = collectionType + "<" + typeClass + ">";
        }

        System.out.println("DtoTypeClass: " + typeClass);
        fieldEntity.setType(typeClass);
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
