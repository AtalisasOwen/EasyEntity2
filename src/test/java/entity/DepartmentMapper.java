package entity;

import org.mapstruct.Mapper;

@Mapper
public class DepartmentMapper {
    public Department fromId(Long id){
        Department department = new Department();
        department.setId(id);
        department.setName("部门"+id);
        return department;
    }

    public Long toId(Department department){
        return department.getId();
    }
}
