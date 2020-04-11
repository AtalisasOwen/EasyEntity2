package com.atalisas.parser;

import com.atalisas.entity.FieldEntity;
import com.atalisas.entity.MappingAnnotationEntity;
import com.atalisas.utils.AnnotationUtils;
import com.atalisas.utils.ElementUtils;
import com.atalisas.utils.FieldMethodUtils;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import java.util.List;
import java.util.logging.Logger;

public class DtoCollectionFieldParser {

    Logger log = Logger.getLogger("DtoCollectionFieldParser");

    // 生成表达:List<T> var1, T.var2, CollectionType var3,
    private String generateGetter(String field) {
        return "get" + FieldMethodUtils.captureName(field);
    }

    private String simplifyCollection(String collectionType){
        String[] ss = collectionType.split("\\.");
        return ss[ss.length-1];
    }

    private String generateExpression(String var1, String var2, String var3, String var4) {
        var1 = generateGetter(var1);
        var2 = generateGetter(var2);
        var4 = simplifyCollection(var4);
        return "java( e." + var1 + "().stream().map(" + var3 + "::" + var2 + ").collect(Collectors.to"+var4+"()) )";
    }

    public MappingAnnotationEntity generateMappingAnnotation(Element innerElement, AnnotationMirror field) {
        log.info(">>>> " + innerElement.toString());
        MappingAnnotationEntity entity = new MappingAnnotationEntity();
        AnnotationValue subFieldType = AnnotationUtils.getAnnotationValue(field, "value");
        log.info(">>>> " + subFieldType.getValue().toString());
        AnnotationValue name = AnnotationUtils.getAnnotationValue(field, "rename");
        entity.setTarget(name.getValue().toString());
        DeclaredType fieldType = (DeclaredType) innerElement.asType();
        List<? extends TypeMirror> fieldOfList = fieldType.getTypeArguments();
        if (fieldOfList.size() == 1) {
            DeclaredType var3 = (DeclaredType) fieldOfList.get(0);
            log.info(">>>> " + var3.toString());
            String var4 = getCollectionType(fieldType);
            log.info(">>>> " + var4);
            String expression = generateExpression(innerElement.toString(),
                    subFieldType.getValue().toString(), var3.toString(), var4);
            log.info("Expression: "+expression);
            entity.setExpression(expression);
            entity.addImportClass(var3.toString()+".class");
            entity.addImportClass(var4+".class");
            entity.addImportClass("java.util.stream.Collectors.class");
        } else {
            throw new IllegalArgumentException("不支持该集合类 " + field);
        }

        return entity;
    }

    private String concatCollectionType(String collectionType, String collectionElementType) {
        return collectionType + "<" + collectionElementType + ">";
    }

    private String getCollectionType(DeclaredType fieldType) {
        String s = fieldType.toString();
        return s.split("<")[0];
    }

    public FieldEntity parse(Element innerElement, AnnotationMirror field) {
        FieldEntity fieldEntity = new FieldEntity();
        AnnotationValue subFieldType = AnnotationUtils.getAnnotationValue(field, "value");
        AnnotationValue name = AnnotationUtils.getAnnotationValue(field, "rename");
        fieldEntity.setName(name.getValue().toString());
        DeclaredType fieldType = (DeclaredType) innerElement.asType();
        log.info("CollectionType: " + fieldType);
        List<? extends TypeMirror> fieldOfList = fieldType.getTypeArguments();
        log.info("getTypeArguments: " + fieldType.getTypeArguments());
        if (fieldOfList.size() == 1) {
            DeclaredType fieldListType = (DeclaredType) fieldOfList.get(0);
            Element fieldTypeElement = ElementUtils.getInnerFieldByName(fieldListType.asElement(), subFieldType.getValue().toString());
            fieldEntity.setType(
                    concatCollectionType(
                            getCollectionType(fieldType),
                            fieldTypeElement.asType().toString()
                    )

            );
            log.info("CollectionDto: " + fieldEntity);
            return fieldEntity;
        } else {
            throw new IllegalArgumentException("不支持该集合类 " + field);
        }
    }
}
