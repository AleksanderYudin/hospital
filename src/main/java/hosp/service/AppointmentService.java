package hosp.service;

import hosp.model.entity.Employee;
import hosp.model.enums.Role;
import hosp.repository.AppointmentRepository;
import hosp.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@EnableScheduling
public class AppointmentService {

    private  final AppointmentRepository appointmentRepository;
    private final EmployeeRepository employeeRepository;
    @Autowired
    public AppointmentService(AppointmentRepository appointmentRepository, EmployeeRepository employeeRepository) {
        this.appointmentRepository = appointmentRepository;
        this.employeeRepository = employeeRepository;
    }

    @Scheduled(fixedDelay = 1000 * 60 * 60 * 24 * 7)
    public void generateAppointments() {
        employeeRepository.findByRole(Role.DOCTOR).forEach(this::saveAppointments);

    }

    public void saveAppointments(Employee doctor){
        for(int i = 0; i < 8; i++) {
            LocalDateTime dateTime = LocalDateTime.of(LocalDateTime.now().getYear(), LocalDateTime.now().getMonth(),
                            LocalDateTime.now().getDayOfMonth(),9 + i,0);
            appointmentRepository.saveAppointments(doctor.getId(), dateTime, true);
        }
    }
}
