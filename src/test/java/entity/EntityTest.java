package entity;

import entity.mapper.EmployeeMapper;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class EntityTest {

    @Test
    public void test(){
        Employee e = new Employee();
        e.setId(1L);
        e.setName("LALALA");
        e.setDprtId(1L);
        List<Long> dprts = new ArrayList<>();
        dprts.add(2L);
        dprts.add(3L);
        e.setDprtIds(dprts);
        entity.dto.EmployeeDto dto = EmployeeMapper.INSTANCE.toDto(e);
        System.out.println(dto);
    }
}
