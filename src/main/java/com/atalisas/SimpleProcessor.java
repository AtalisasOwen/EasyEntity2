package com.atalisas;

import com.atalisas.annotation.AutoController;
import com.atalisas.entity.*;
import com.atalisas.parser.AutoControllerParser;
import com.atalisas.parser.AutoRepositoryParser;
import com.atalisas.parser.DtoParser;
import com.atalisas.utils.AnnotationUtils;
import com.atalisas.utils.ElementUtils;
import com.atalisas.utils.JavaFileWriter;
import com.google.auto.service.AutoService;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.type.ExecutableType;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@SupportedAnnotationTypes(value = {"com.atalisas.annotation.Dto",
                                    "com.atalisas.annotation.AutoRepository",
                                    "com.atalisas.annotation.AutoController"})
@SupportedSourceVersion(value = SourceVersion.RELEASE_8)
@AutoService(Processor.class)
public class SimpleProcessor extends AbstractProcessor {

    public final static Logger log = Logger.getLogger("SimpleProcessor");

    DtoParser dtoParser = new DtoParser();
    AutoRepositoryParser repositoryParser = new AutoRepositoryParser();
    AutoControllerParser controllerParser = new AutoControllerParser();


    public void processAutoControllerAnnotation(Set<? extends Element> classes){
        List<ControllerClassEntity> classEntities = controllerParser.parse(classes);
        classEntities.stream().forEach(controllerClassEntity -> {
            try {
                JavaFileObject builderFile = processingEnv.getFiler().
                        createSourceFile(controllerClassEntity.getPackageName() + "." +controllerClassEntity.getClassName());
                JavaFileWriter.writeJavaFile(builderFile, controllerClassEntity.toString());
                log.fine(controllerClassEntity.toString());

            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }

    public void processAutoRepositoryAnnotation(Set<? extends Element> classes){
        List<RepositoryClassEntity> classEntities = repositoryParser.parse(classes);
        classEntities.stream().forEach(classEntity -> {
            try {
                JavaFileObject builderFile = processingEnv.getFiler().
                        createSourceFile(classEntity.getPackageName()+"."+classEntity.getClassName());
                JavaFileWriter.writeJavaFile(builderFile, classEntity.toString());
                log.fine(classEntity.toString());

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void processDtoAnnotations(Set<? extends Element> classes){
        List<ClassEntity> classEntities = dtoParser.parse(classes);
        List<MapStructMapperEntity> mapStructMapperEntities = dtoParser.generateMapStructMapperEntity(classes);
        classEntities.stream().forEach(classEntity -> {
            classEntity.addToStringMethod();
            try {
                JavaFileObject builderFile = processingEnv.getFiler().
                        createSourceFile(classEntity.getPackageName()+"."+classEntity.getClassName());
                JavaFileWriter.writeJavaFile(builderFile, classEntity.toString());
                log.fine(classEntity.toString());

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        mapStructMapperEntities.stream().forEach(mapperEntity -> {
            try {
                String usesClasses = DependencyGraph.getUsesClassString(mapperEntity.getPackageName(), mapperEntity.getClassName());
                if (usesClasses != null){
                    mapperEntity.addUsesClass(usesClasses);
                }
                JavaFileObject builderFile = processingEnv.getFiler().
                        createSourceFile(mapperEntity.getPackageName()+"."+mapperEntity.getClassName());
                JavaFileWriter.writeJavaFile(builderFile, mapperEntity.toString());
                log.fine(mapperEntity.toString());

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }


    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        for (TypeElement typeElement : annotations) {
            Set<? extends Element> classes = roundEnv.getElementsAnnotatedWith(typeElement);
            if (typeElement.getSimpleName().contentEquals("Dto")){
                processDtoAnnotations(classes);
            } else if (typeElement.getSimpleName().contentEquals("AutoRepository")){
                processAutoRepositoryAnnotation(classes);
            }
        }
        for (TypeElement typeElement : annotations) {
            Set<? extends Element> classes = roundEnv.getElementsAnnotatedWith(typeElement);
            if (typeElement.getSimpleName().contentEquals("AutoController")){
                processAutoControllerAnnotation(classes);
            }
        }
        return true;
    }


}
