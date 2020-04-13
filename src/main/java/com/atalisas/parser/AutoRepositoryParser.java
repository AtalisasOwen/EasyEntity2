package com.atalisas.parser;

import com.atalisas.entity.DependencyGraph;
import com.atalisas.entity.RepositoryClassEntity;
import com.atalisas.entity.RepositoryExtendsEntity;
import com.atalisas.utils.AnnotationUtils;
import com.atalisas.utils.ElementUtils;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class AutoRepositoryParser {

    JpaParser jpaParser = new JpaParser();

    public List<RepositoryClassEntity> parse(Set<? extends Element> classes){
        List<RepositoryClassEntity> classEntities = new ArrayList<>();
        for (Element s : classes){
            System.out.println("Entity Class: " + s.asType().toString());
            AnnotationMirror reposEntity = ElementUtils.judgeFieldWithAnnotion(s, "AutoRepository");
            System.out.println("AnnotationMirror: " + reposEntity);
            AnnotationValue strategy = AnnotationUtils.getAnnotationValue(reposEntity, "strategy");
            System.out.println("Strategy: " + strategy);
            if (strategy == null){
                RepositoryClassEntity repositoryClassEntity = jpaParser.parse(s);
                classEntities.add(repositoryClassEntity);
            }
        }

        return classEntities;
    }
}
