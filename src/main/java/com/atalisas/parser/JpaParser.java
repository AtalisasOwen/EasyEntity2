package com.atalisas.parser;

import com.atalisas.entity.RepositoryClassEntity;
import com.atalisas.entity.RepositoryExtendsEntity;
import com.atalisas.utils.ElementUtils;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import java.util.ArrayList;
import java.util.List;

public class JpaParser {

    private String toRepositoryClass(String entityType){
        String[] ss = entityType.split("\\.");
        return ss[ss.length-1]+"Repository";
    }

    private String toRepositoryPackage(String entityType){
        String[] ss = entityType.split("\\.");
        if (ss.length <= 2){
            return "repository";
        }else{
            List<String> s = new ArrayList<>();
            for (int i = 0; i < ss.length - 2; i++){
                s.add(ss[i]);
            }
            s.add("repository");
            return String.join(".", s);
        }
    }

    public RepositoryClassEntity parse(Element s){
        RepositoryClassEntity repositoryClassEntity = new RepositoryClassEntity();
        repositoryClassEntity.setPackageName(toRepositoryPackage(s.asType().toString()));
        repositoryClassEntity.setClassName(toRepositoryClass(s.asType().toString()));
        for (Element innerElement : s.getEnclosedElements()){
            AnnotationMirror idEntity = ElementUtils.judgeFieldWithAnnotion(innerElement, "Id");
            if (idEntity != null){
                System.out.println(innerElement.asType().toString());
                // 生成JpaRepository
                RepositoryExtendsEntity extendsEntity = new RepositoryExtendsEntity();
                extendsEntity.setClassName("org.springframework.data.jpa.repository.JpaRepository");
                extendsEntity.addGenericType(s.asType().toString());
                extendsEntity.addGenericType(innerElement.asType().toString());
                repositoryClassEntity.addextendsEntities(extendsEntity);
                // 生成JpaSpecificationExecutor
                RepositoryExtendsEntity extendsEntity2 = new RepositoryExtendsEntity();
                extendsEntity2.setClassName("org.springframework.data.jpa.repository.JpaSpecificationExecutor");
                extendsEntity2.addGenericType(s.asType().toString());
                repositoryClassEntity.addextendsEntities(extendsEntity2);
                break;
            }
        }
        return repositoryClassEntity;
    }
}
