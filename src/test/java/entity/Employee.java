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
