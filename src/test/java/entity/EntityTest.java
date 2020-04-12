package entity;

import entity.mapper.EnployeeMapper;
import org.junit.Test;

public class EntityTest {

    @Test
    public void test(){
        Enployee e = new Enployee();
        e.setId(1L);
        e.setName("LALALA");
        Department department = new Department();
        department.setId(12L);
        department.setName(":sdada");
        e.setDprt(department);
        entity.dto.EnployeeDto dto = EnployeeMapper.INSTANCE.toDto(e);
        System.out.println(dto);
    }
}
