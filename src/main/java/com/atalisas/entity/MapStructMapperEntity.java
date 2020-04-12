package com.atalisas.entity;

import java.net.StandardSocketOptions;
import java.util.ArrayList;
import java.util.List;

public class MapStructMapperEntity {
    String packageName;
    String className;
    List<String> importClasses;
    List<String> mapperImportClasses;
    List<MapperMethodEntity> mapperMethodEntities;
    List<String> usesClasses;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("package " + packageName + ";\n\n");
        importClasses.stream().forEach(importClass -> {
            sb.append("import " + importClass + ";\n");
        });
        sb.append("\n");
        String mapperImport = String.join(", ", mapperImportClasses);
        sb.append("@Mapper(imports = {" + mapperImport+"}");
        if (usesClasses != null){
            String useClassesStr = String.join(", ", usesClasses);
            String ss = " ,uses = {"+useClassesStr+"}";
            sb.append(ss);
        }
        sb.append(")\n");
        sb.append("public interface " + className + " {\n" +
                "    "+className+" INSTANCE = Mappers.getMapper( "+className+".class );\n\n");
        if (mapperMethodEntities != null){
            mapperMethodEntities.stream().forEach(mapperMethodEntity -> {
                sb.append(mapperMethodEntity);
                sb.append("\n");
                sb.append("    @InheritInverseConfiguration\n");
                sb.append("    "+mapperMethodEntity.getParaType() + " " + "toEnity(" + mapperMethodEntity.getReturnType() + " dto);");
                sb.append("\n");
            });
        }
        sb.append("}\n");
        return sb.toString();
    }

    public void addUsesClass(String usesClass){
        if (this.usesClasses == null){
            this.usesClasses = new ArrayList<>();
        }
        this.usesClasses.add(usesClass);
    }

    public void addImportClass(String importClass){
        if (this.importClasses == null){
            this.importClasses = new ArrayList<>();
        }
        this.importClasses.add(importClass);
    }

    public void addMapperImportClass(String mapperImportClass){
        if (this.mapperImportClasses == null){
            this.mapperImportClasses = new ArrayList<>();
        }
        this.mapperImportClasses.add(mapperImportClass);
    }

    public void addMapperMethodEntity(MapperMethodEntity entity){
        if (this.mapperMethodEntities == null){
            this.mapperMethodEntities = new ArrayList<>();
        }
        this.mapperMethodEntities.add(entity);
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

    public List<String> getImportClasses() {
        return importClasses;
    }

    public void setImportClasses(List<String> importClasses) {
        this.importClasses = importClasses;
    }

    public List<String> getMapperImportClasses() {
        return mapperImportClasses;
    }

    public void setMapperImportClasses(List<String> mapperImportClasses) {
        this.mapperImportClasses = mapperImportClasses;
    }

    public List<MapperMethodEntity> getMapperMethodEntities() {
        return mapperMethodEntities;
    }

    public void setMapperMethodEntities(List<MapperMethodEntity> mapperMethodEntities) {
        this.mapperMethodEntities = mapperMethodEntities;
    }
}
