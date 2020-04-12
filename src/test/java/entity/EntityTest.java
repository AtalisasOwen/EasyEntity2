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
