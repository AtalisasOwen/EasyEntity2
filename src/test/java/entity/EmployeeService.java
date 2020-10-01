package entity;

import com.atalisas.annotation.AutoController;
import entity.mapper.EmployeeMapper;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

@AutoController(Employee.class)
public class EmployeeService {

    EmployeeMapper mapper = EmployeeMapper.INSTANCE;

    private void test(){
        return;
    }


    //@GetMapping("/employees")
    public List<Employee> getEmployees(){
        return null;
    }

    //@GetMapping("/employee/{id}")
    public Employee getEmployee(Long id){
        return new Employee();
    }

    //@GetMapping("/employee/{id}")
    public Employee getEmployee(List<Department> id){
        return null;
    }

    public Employee getEmployee(String name){
        return null;
    }

    public Employee getEmployee(Long dprt, Pageable pageable){
        return null;
    }

    //@PostMapping("/employee")
    public Employee postEmployee(Employee employee){
        return null;
    }

    //@PostMapping("/employee/dprt/{dprtId}/team/{teamId}")
    public Employee addEmployeeByDprtByTeam(Long dprtId,Long teamId, Employee employee){
        //EmployeeByDprtByTeam
        //[ employee dprt team] [ dprtId teamId ]
        // /employee/dprt/{dprtId}/team/{teamId}
        return null;
    }

    //@DeleteMapping("/employee/{id}")
    public void deleteEmployee(Long id){
        return;
    }

}
