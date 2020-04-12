package com.atalisas.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RepositoryClassEntity {
    String packageName;
    String className;
    List<RepositoryExtendsEntity> extendsEntities;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("package " + packageName + ";\n\n");
        sb.append("public interface ");
        sb.append(className);
        if (extendsEntities != null){
            sb.append(" extends ");
            String extendsStr = String.join(", ", this.extendsEntities.stream().map(RepositoryExtendsEntity::toString).collect(Collectors.toList()));
            sb.append(extendsStr);
        }
        sb.append(" {");

        sb.append("}\n");
        return sb.toString();
    }

    public void addextendsEntities(RepositoryExtendsEntity extendsEntity){
        if (this.extendsEntities == null){
            this.extendsEntities = new ArrayList<>();
            this.extendsEntities.add(extendsEntity);
        } else {
            this.extendsEntities.add(extendsEntity);
        }
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public List<RepositoryExtendsEntity> getExtendsEntities() {
        return extendsEntities;
    }

    public void setExtendsEntities(List<RepositoryExtendsEntity> extendsEntities) {
        this.extendsEntities = extendsEntities;
    }
}
