package hosp.service;

import hosp.model.entity.Employee;
import hosp.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {
    @Autowired
    private UserService userService;
    @MockBean
    private EmployeeRepository repository;
    @MockBean
    private PasswordEncoder passwordEncoder;

    @Test
    void addUser() {
        Employee user = new Employee();
        boolean isUserCreated = userService.addUser(user);
        assertTrue(isUserCreated);
    }

    @Test
    void addUserFailTest() {
        Employee user = new Employee();
        user.setUsername("smith");
        Mockito.doReturn(new Employee())
                .when(repository)
                .findByUsername("smith");

        boolean isUserCreated = userService.addUser(user);

        assertFalse(isUserCreated);
    }
}