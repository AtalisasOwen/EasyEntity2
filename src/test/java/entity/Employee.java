package entity;

import com.atalisas.annotation.AutoRepository;
import com.atalisas.annotation.Dto;
import com.atalisas.annotation.DtoCustomField;
import com.atalisas.annotation.DtoField;
import lombok.Data;

import java.util.List;

@Data
@Dto
@AutoRepository
public class Employee {
    @Id
    Long id;
    String name;
    @DtoCustomField(rename = "dprt", typeClass = Department.class, mapperClass = DepartmentMapper.class)
    Long dprtId;
    @DtoCustomField(rename = "dprts", typeClass = Department.class, mapperClass = DepartmentMapper.class)
    List<Long> dprtIds;

}
