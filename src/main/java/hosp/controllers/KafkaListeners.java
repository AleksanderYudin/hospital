package hosp.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hosp.model.entity.Appointment;
import hosp.model.entity.DoctorDto;
import hosp.model.entity.Employee;
import hosp.model.enums.Role;
import hosp.repository.AppointmentRepository;
import hosp.repository.EmployeeRepository;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

import java.time.LocalDateTime;
import java.util.*;

@EnableKafka
@Component
public class KafkaListeners {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final EmployeeRepository employeeRepository;
    private final AppointmentRepository appointmentRepository;

    ObjectMapper objectMapper = new ObjectMapper();

    public KafkaListeners(KafkaTemplate<String, String> kafkaTemplate, EmployeeRepository employeeRepository, AppointmentRepository appointmentRepository) {
        this.kafkaTemplate = kafkaTemplate;
        this.employeeRepository = employeeRepository;
        this.appointmentRepository = appointmentRepository;
    }

    @KafkaListener(topics="hospital2_to_hospital_doctorsList")
    public void orderListener(ConsumerRecord<String, String> record){
//        System.out.println(record.partition());
//        System.out.println(record.key());
//        System.out.println(record.value());

        if(record.value().contains("doctorsList")) {
            List<DoctorDto> employeesList = new ArrayList<>();
            employeeRepository.findByRole(Role.DOCTOR)
                    .forEach(doctor -> {
                        employeesList.add(new DoctorDto(doctor.getId(),
                                doctor.getFirstName() + ' ' + doctor.getSecondName(),
                                doctor.getQualification(),
                                null));
                    });
            String doctorsList = "";
            try {
                doctorsList = objectMapper.writeValueAsString(employeesList);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            ListenableFuture<SendResult<String, String>> future = kafkaTemplate
                    .send("hospital_to_hospital2_doctorsList", record.key(), doctorsList);
            future.addCallback(System.out::println, System.err::println);
            kafkaTemplate.flush();
        }

        else {
            System.out.println(record.value());
            Long id = new Scanner(record.value()).nextLong();
            List<Appointment> appointments = appointmentRepository.findAllByDoctorId(id);
            Employee employee = employeeRepository.findById(id).orElse(null);
            DoctorDto doctorDto = new DoctorDto(id, employee.getSecondName() + " " +
                    employee.getFirstName(), employee.getQualification(), null);
            List<LocalDateTime> dates = new ArrayList<>();
            appointments.forEach(a -> {
                dates.add(a.getDateTime());
            });
            doctorDto.setDates(dates);

            String doctor = "";
            try {
                doctor = objectMapper.writeValueAsString(DoctorDto.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            ListenableFuture<SendResult<String, String>> future = kafkaTemplate
                    .send("hospital_to_hospital2_doctorsList", record.key(), doctor);
            future.addCallback(System.out::println, System.err::println);
            kafkaTemplate.flush();
        }
    }
}
