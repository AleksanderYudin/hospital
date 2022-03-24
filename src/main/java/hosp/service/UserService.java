package hosp.service;

import hosp.model.entity.Employee;
import hosp.repository.EmployeeRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class UserService implements UserDetailsService {

    private final EmployeeRepository employeeRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return employeeRepository.findByUsername(username);
    }

    public boolean addUser(Employee employee){
        Employee employeeFromDB = employeeRepository.findByUsername(employee.getUsername());
        if(employeeFromDB != null) return false;

        employee.setActive(true);
        employee.setPassword(passwordEncoder.encode(employee.getPassword()));
        employeeRepository.save(employee);
        log.info("Зарегистрирован новый пользователь, " + employee.getUsername());
        return true;
    }

}
