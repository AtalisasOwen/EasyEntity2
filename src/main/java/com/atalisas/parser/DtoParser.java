package com.atalisas.parser;

import com.atalisas.entity.*;
import com.atalisas.utils.AnnotationUtils;
import com.atalisas.utils.ClassUtils;
import com.atalisas.utils.ElementUtils;
import com.atalisas.utils.FieldMethodUtils;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

public class DtoParser {

    public final static String[] ANNOTIONS = {"DtoField", "DtoIgnore", "DtoCollectionField", "DtoEntityField", "DtoCustomField"};
    public final static Logger log = Logger.getLogger("DtoParser");

    DtoFieldParser dtoFieldParser = new DtoFieldParser();
    NormalFieldParser normalFieldParser = new NormalFieldParser();
    DtoCollectionFieldParser collectionFieldParser = new DtoCollectionFieldParser();
    DtoEntityFieldParser entityFieldParser = new DtoEntityFieldParser();
    DtoCustomFieldParser customFieldParser = new DtoCustomFieldParser();


    public MappingAnnotationEntity generateMappingAnnotation(Element innerElement, String parent){
        for (String ann : ANNOTIONS) {
            AnnotationMirror dtoField = ElementUtils.judgeFieldWithAnnotion(innerElement, ann);
            log.info("注解: " + dtoField);
            if (dtoField != null) {
                if (ann.equals("DtoField")){
                    MappingAnnotationEntity s = dtoFieldParser.generateMappingAnnotation(innerElement, dtoField);
                    log.info(s.toString());
                    return s;
                } else if (ann.equals("DtoIgnore")){
                    return null;
                } else if (ann.equals("DtoCollectionField")){
                    MappingAnnotationEntity s = collectionFieldParser.generateMappingAnnotation(innerElement, dtoField);
                    log.info(s.toString());
                    return s;
                } else if (ann.equals("DtoEntityField")){
                    MappingAnnotationEntity s = entityFieldParser.generateMappingAnnotation(innerElement, dtoField);
                    log.info(s.toString());
                    DependencyGraph.addDependency(parent, innerElement.asType().toString());
                    return s;
                } else if (ann.equals("DtoCustomField")){
                    MappingAnnotationEntity s = customFieldParser.generateMappingAnnotation(innerElement, dtoField);
                    AnnotationValue mapperClass = AnnotationUtils.getAnnotationValue(dtoField, "mapperClass");
                    String mapperClassString = mapperClass.getValue().toString();
                    System.out.println("Input Dependency Input: " + mapperClassString);
                    DependencyGraph.addUserInputDependency(parent, mapperClassString+".class");
                    return s;
                }
            }
        }
        return null;
    }


    public FieldEntity parseAnnotations(Element innerElement) {
        for (String ann : ANNOTIONS) {
            AnnotationMirror dtoField = ElementUtils.judgeFieldWithAnnotion(innerElement, ann);
            if (dtoField != null) {
                if (ann.equals("DtoField")){
                    return dtoFieldParser.parse(innerElement, dtoField);
                } else if (ann.equals("DtoIgnore")){
                    return null;
                } else if (ann.equals("DtoCollectionField")){
                    return collectionFieldParser.parse(innerElement, dtoField);
                } else if (ann.equals("DtoEntityField")){
                    return entityFieldParser.parse(innerElement, dtoField);
                } else if (ann.equals("DtoCustomField")){
                    return customFieldParser.parse(innerElement, dtoField);
                }
            }
        }
        return normalFieldParser.parse(innerElement);
    }

    public List<ClassEntity> parse(Set<? extends Element> elements) {
        List<ClassEntity> classEntities = new ArrayList<>();
        for (Element s : elements) { // 找到注解了Dto的类

            ClassEntity classEntity = new ClassEntity();
            classEntity.setPackageName(ClassUtils.getPackage(s.toString()) + ".dto");
            classEntity.setClassName(ClassUtils.getSimpleClassName(s.toString()) + "Dto");
            classEntity.setFieldEntities(new ArrayList<>());
            classEntity.setMethodEntities(new ArrayList<>());

            List<Element> fieldEles = ElementUtils.getInnerField(s);
            for (Element innerElement : fieldEles) {
                FieldEntity fieldEntity = parseAnnotations(innerElement);
                if (fieldEntity == null){
                    continue;
                }
                MethodEntity setter = FieldMethodUtils.makeSetter(fieldEntity);
                MethodEntity getter = FieldMethodUtils.makeGetter(fieldEntity);
                classEntity.getFieldEntities().add(fieldEntity);
                classEntity.getMethodEntities().add(setter);
                classEntity.getMethodEntities().add(getter);

            }
            classEntities.add(classEntity);

        }
        return classEntities;
    }


    public List<MapStructMapperEntity> generateMapStructMapperEntity(Set<? extends Element> elements){
        List<MapStructMapperEntity> entities = new ArrayList<>();
        for (Element s : elements) { // 找到注解了Dto的类

            System.out.println("ELEMENT: " + s.getSimpleName().toString());

            MapStructMapperEntity mapperEntity = new MapStructMapperEntity();
            mapperEntity.setPackageName(ClassUtils.getPackage(s.toString()) + ".mapper");
            mapperEntity.setClassName(ClassUtils.getSimpleClassName(s.toString()) + "Mapper");
            mapperEntity.addImportClass("org.mapstruct.Mapper");
            mapperEntity.addImportClass("org.mapstruct.Mapping");
            mapperEntity.addImportClass("org.mapstruct.factory.Mappers");
            mapperEntity.addImportClass("org.mapstruct.InheritInverseConfiguration");
            mapperEntity.setMapperImportClasses(new ArrayList<>());


            MapperMethodEntity methodEntity = new MapperMethodEntity();
            String entityPackage = ClassUtils.getPackage(s.toString());
            String entityClass = ClassUtils.getSimpleClassName(s.toString());
            String dtoPackage = entityPackage + ".dto";
            String dtoClass = entityClass + "Dto";
            methodEntity.setReturnType(dtoPackage+"."+dtoClass);
            methodEntity.setParaType(entityPackage+"."+entityClass);
            methodEntity.setMethodName("toDto");


            List<Element> fieldEles = ElementUtils.getInnerField(s);

            for (Element innerElement : fieldEles) {
                // 解析获取 @Mapping 注解
                MappingAnnotationEntity annotationEntity = generateMappingAnnotation(innerElement, s.asType().toString());
                if (annotationEntity == null){
                    continue;
                }
                log.info("得到注解Mapping" + annotationEntity);
                // 将注解表达式中的引入类，存放到Mapper全局
                if (annotationEntity.getImportClass() != null){
                    mapperEntity.getMapperImportClasses().addAll(annotationEntity.getImportClass());
                }
                methodEntity.addMappingAnnotationEntity(annotationEntity);
            }
            mapperEntity.addMapperMethodEntity(methodEntity);
            entities.add(mapperEntity);
        }

        return entities;
    }
}
