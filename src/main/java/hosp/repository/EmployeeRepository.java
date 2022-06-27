package hosp.repository;

import hosp.model.entity.Employee;
import hosp.model.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Employee findByUsername(String username);
    List<Employee> findByRole(Role role);
}
