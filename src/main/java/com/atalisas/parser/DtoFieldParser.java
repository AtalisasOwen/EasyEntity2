package com.atalisas.parser;

import com.atalisas.entity.FieldEntity;
import com.atalisas.entity.MappingAnnotationEntity;
import com.atalisas.utils.AnnotationUtils;
import com.atalisas.utils.ElementUtils;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.type.DeclaredType;
import java.util.logging.Logger;

public class DtoFieldParser {

    public final static Logger log = Logger.getLogger("DtoFieldParser");

    public MappingAnnotationEntity generateMappingAnnotation(Element innerElement, AnnotationMirror field) {
        MappingAnnotationEntity annotationEntity = new MappingAnnotationEntity();
        AnnotationValue subFieldType = AnnotationUtils.getAnnotationValue(field, "value");
        AnnotationValue name = AnnotationUtils.getAnnotationValue(field, "rename");
        annotationEntity.setSource(innerElement.getSimpleName().toString()+"."+subFieldType.getValue().toString());
        annotationEntity.setTarget(name.getValue().toString());
        return annotationEntity;
    }

    public FieldEntity parse(Element innerElement, AnnotationMirror field) {
        FieldEntity fieldEntity = new FieldEntity();
        AnnotationValue subFieldType = AnnotationUtils.getAnnotationValue(field, "value");
        AnnotationValue name = AnnotationUtils.getAnnotationValue(field, "rename");
        fieldEntity.setName(name.getValue().toString());
        DeclaredType fieldType = (DeclaredType) innerElement.asType();
        Element fieldTypeElement = ElementUtils.getInnerFieldByName(fieldType.asElement(), subFieldType.getValue().toString());
        fieldEntity.setType(fieldTypeElement.asType().toString());
        return fieldEntity;
    }
}
