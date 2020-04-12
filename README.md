### EasyEntity
#### 项目目标
在Java后端开发中，虽然借助Spring Boot等框架，已经不需要过于配置；但是在项目中，往往还有很多比较无脑的重复性代码要写。

受启发与lombok，mapstruct，本项目旨在减少重复性代码的编写，如Dto类，Mapper类，JpaRepository类，未来甚至可以生成Service，Controller类。
#### 编译
```
git clone XXXXXXXXXXXx
mvn package
```

#### 安装
```
    <dependencies>
        ......
        <dependency>
            <groupId>org.mapstruct</groupId>
            <artifactId>mapstruct</artifactId>
            <version>${org.mapstruct.version}</version>
        </dependency>
        <dependency>
            <groupId>org.mapstruct</groupId>
            <artifactId>mapstruct-processor</artifactId>
            <version>${org.mapstruct.version}</version>
            <scope>provided</scope>
        </dependency>
        <dependency>
            <groupId>com.atalisas</groupId>
            <artifactId>EasyEntity</artifactId>
            <version>0.5</version>
            <scope>system</scope>
            <systemPath>${project.basedir}/lib/EasyEntity-0.5.jar</systemPath>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            ....
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <configuration>
                    <source>8</source>
                    <target>8</target>
                    <compilerArguments>
                        <extdirs>${project.basedir}/lib</extdirs>
                    </compilerArguments>
                </configuration>
            </plugin>
        </plugins>
    </build>
```

### 使用
##### 编写Entity类
在Entity类中写上@Dto注解，表示需要生成对应的Dto和Mapper类
在Entity类的字段上加上
- @DtoField: 将该类型转换为该字段内部的存在类型，如下例，将dprt.name在Dto类中转换为dprtName
```java
@DtoField(value = "name", rename = "dprtName")
Department dprt;
```
- @DtoIgnore：忽略该字段
- @DtoCollectionField：集合类型的@DtoField，如List<Department> dprts 变为 List<Srting> dprtName
- @DtoEntityField：常用与嵌套Dto，如List<Department> dprts 变为 List<DepartmentDto> dprtDtos


##### 完整例子如下
```java
package entity;

import com.atalisas.annotation.Dto;
import com.atalisas.annotation.DtoField;
import lombok.Data;

@Data
@Dto
public class Employee {
    Long id;
    String name;
    @DtoField(value = "name", rename = "dprtName")
    Department dprt;

    public void test(){
        dprt = new Department();
    }
}
```

```java

package entity;

import lombok.Data;

@Data
public class Department {
    Long id;
    String name;
}

```

#### 编译
`mvn clean compile`

#### 自动生成的如下
EmployeeDto
```java
package entity.dto;

public class EmployeeDto {
    private java.lang.Long id;
    private java.lang.String name;
    private java.lang.String dprtName;

    public void setId(java.lang.Long id) {
        this.id = id;
    }

    public java.lang.Long getId() {
        return this.id;
    }

    public void setName(java.lang.String name) {
        this.name = name;
    }

    public java.lang.String getName() {
        return this.name;
    }

    public void setDprtName(java.lang.String dprtName) {
        this.dprtName = dprtName;
    }

    public java.lang.String getDprtName() {
        return this.dprtName;
    }

    public String toString() {
    return "EmployeeDto{" +
                "id=" + id +
                ", name=" + name +
                ", dprtName=" + dprtName +
                '}';
    }

}
```
EmployeeMapper
```java
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.mapstruct.InheritInverseConfiguration;

@Mapper(imports = {})
public interface EmployeeMapper {
    EmployeeMapper INSTANCE = Mappers.getMapper( EmployeeMapper.class );

    @Mapping(source = "dprt.name", target = "dprtName")
    entity.dto.EmployeeDto toDto(entity.Employee e);

    @InheritInverseConfiguration
    entity.Employee toEnity(entity.dto.EmployeeDto dto);
}

```

#### 使用自动生成的Dto
```java
package entity;

import entity.mapper.EmployeeMapper;
import org.junit.Test;

public class EntityTest {

    @Test
    public void test(){
        Employee e = new Employee();
        e.setId(1L);
        e.setName("LALALA");
        Department department = new Department();
        department.setId(12L);
        department.setName(":sdada");
        e.setDprt(department);
        entity.dto.EmployeeDto dto = EmployeeMapper.INSTANCE.toDto(e);
        System.out.println(dto);
    }
}
```