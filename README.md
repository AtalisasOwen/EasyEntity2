### EasyEntity
#### 项目目标
在Java后端开发中，一般有Entity层，供外部传输的Dto层，Dto与Entity进行转换的Mapper层，Repository层，Service层，Controller层
- Lombok自动生成了Entity中的Setter/Getter等方法
- mapstruct使得开发者只需要很少的配置，就可根据Dto和Entity进行转换
- Spring Data Jpa提供一个封装很多方法的Repository层

但是用户还是需要自己编写Entity，Dto，Mapper接口，Service，Controller，本项目致力于使得开发者致力于Entity和Service层的设计，而根据Entity和Service的注解和方法签名，在编译期间自动生成代码
- Dto层，Mapper层可通过注解，由Entity的代码和框架注解自动生成
- Repository层：自动生成Entity的JpaRepository，后续可能会添加MyBatis版本
- Controller层：根据Service的public方法，以及方法名自动生成代码

#### 安装
```
    <dependencies>
        ......
        <!--Spring Boot, JPA 依赖-->
        <dependency>
            <groupId>io.github.atalisasowen</groupId>
            <artifactId>EasyEntity2</artifactId>
            <version>0.7.5</version>
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


#### 注解说明
`@Dto`：生成该Entity的Dto类和Mapper类
- `@DtoField`: 将引用类型的字段，转为其内部的引用类型，如`Department{dprt.id} => Long{id}`
- `@DtoIgnore`: 忽略该字段
- `@DtoCollectionField`: 类似@DtoField, 不过是List或者Set，如`List<Department>{dprt.id} => List<Long>{id}`
- `@DtoEntityField`: 表示该字段也是一个Dto， 如`Department => DepartmentDto, List<Department> => List<DepartmentDto>`
- `@DtoCustomField`: 表示该字段，需要自定义Mapper，常用于微服务的外部调用，如`Long id => Department dprt`

Entity类
```java
@Dto
@Data
@AllArgsConstructor
@NoArgsConstructor
@AutoRepository
@Entity
public class Employee {
    @Id
    Long id;
    String name;
    @DtoField(value = "id", rename = "dprtId")
    @OneToOne
    Department department;
    @DtoEntityField(rename = "lessonSummary")
    @OneToMany
    List<Lesson> lessons;
}

```

生成的Dto和Mapper
```java
public class EmployeeDto {
    private java.lang.Long id;
    private java.lang.String name;
    private java.lang.Long dprtId;
    private java.util.List<com.example.demo.entity.dto.LessonDto> lessonSummary;

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

    public void setDprtId(java.lang.Long dprtId) {
        this.dprtId = dprtId;
    }

    public java.lang.Long getDprtId() {
        return this.dprtId;
    }

    public void setLessonSummary(java.util.List<com.example.demo.entity.dto.LessonDto> lessonSummary) {
        this.lessonSummary = lessonSummary;
    }

    public java.util.List<com.example.demo.entity.dto.LessonDto> getLessonSummary() {
        return this.lessonSummary;
    }

    public String toString() {
    return "EmployeeDto{" +
                "id=" + id +
                ", name=" + name +
                ", dprtId=" + dprtId +
                ", lessonSummary=" + lessonSummary +
                '}';
    }

}

@Mapper(imports = {} ,uses = {com.example.demo.entity.mapper.LessonMapper.class})
public interface EmployeeMapper {
    EmployeeMapper INSTANCE = Mappers.getMapper( EmployeeMapper.class );

    @Mapping(source = "department.id", target = "dprtId")
    @Mapping(source = "lessons", target = "lessonSummary")
    com.example.demo.entity.dto.EmployeeDto toDto(com.example.demo.entity.Employee e);

    @InheritInverseConfiguration
    com.example.demo.entity.Employee toEntity(com.example.demo.entity.dto.EmployeeDto dto);
}

```



`@AutoRepository`：生成该Entity的JpaRepository，继承JpaRepository， JpaSpecificationExecutor
- 后续会添加不同的生成策略，如Mybatis等

`@AutoController`：该注解针对Service，根据Service的方法名，返回值等，自动生成Controller层

Service：
```java
public class EmployeeService {
    // ...
    
    public Employee getEmployee(Long id){
            return employees.stream().filter(e -> e.getId().equals(id)).collect(Collectors.toList()).get(0);
    }
    
    public List<Employee> getEmployees(){
            return employees;
    }
    
    public Department addDepartment(Department department){
            return departmentRepository.save(department);
    }
    
    public void deleteDepartment(Long id){
    }

}
```

自动生成的Controller
```java
@RestController
@RequestMapping("/api")
public class EmployeeController {
    @Autowired
    com.example.demo.service.EmployeeService service;

    com.example.demo.entity.mapper.EmployeeMapper mapper = com.example.demo.entity.mapper.EmployeeMapper.INSTANCE;

    @GetMapping("/employee/{id}")
    public com.example.demo.entity.dto.EmployeeDto getEmployee(@PathVariable("id") java.lang.Long id)  {
        return mapper.toDto(service.getEmployee(id));
    }

    @GetMapping("/employees")
    public java.util.List<com.example.demo.entity.dto.EmployeeDto> getEmployees()  {
        return service.getEmployees().stream().map(mapper::toDto).collect(Collectors.toList());
    }

    @PostMapping("/department")
    public com.example.demo.entity.Department addDepartment(@Valid @RequestBody com.example.demo.entity.Department department)  {
        return service.addDepartment(department);
    }

    @DeleteMapping("/department/{id}")
    public void deleteDepartment(@PathVariable("id") java.lang.Long id)  {
        service.deleteDepartment(id);
    }
}
```