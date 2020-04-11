package com.atalisas.parser;

import com.atalisas.entity.FieldEntity;

import javax.lang.model.element.Element;

public class NormalFieldParser {

    public FieldEntity parse(Element innerElement){
        FieldEntity fieldEntity = new FieldEntity();
        fieldEntity.setName(innerElement.getSimpleName().toString());
        fieldEntity.setType(innerElement.asType().toString());
        return fieldEntity;
    }
}
