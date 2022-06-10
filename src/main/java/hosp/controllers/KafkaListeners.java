package hosp.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hosp.model.entity.DoctorDto;
import hosp.model.enums.Role;
import hosp.repository.EmployeeRepository;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@EnableKafka
@Component
public class KafkaListeners {
    @Autowired
    private final KafkaTemplate<String, String> kafkaTemplate;
    @Autowired
    private final EmployeeRepository employeeRepository;

    ObjectMapper objectMapper = new ObjectMapper();

    public KafkaListeners(KafkaTemplate<String, String> kafkaTemplate, EmployeeRepository employeeRepository) {
        this.kafkaTemplate = kafkaTemplate;
        this.employeeRepository = employeeRepository;
    }

    @KafkaListener(topics="hospital2_to_hospital_doctorsList")
    public void orderListener(ConsumerRecord<String, String> record){
//        System.out.println(record.partition());
//        System.out.println(record.key());
//        System.out.println(record.value());
        System.out.println("Request with uuid = " + record.key() + " received...");
        Thread newThread = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("CurrentThread" + Thread.currentThread().toString() + ", time: " + OffsetDateTime.now());
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
                    System.out.println("Response with uuid = " + record.key() + " transmit...");
                    future.addCallback(System.out::println, System.err::println);
                    kafkaTemplate.flush();
                    System.out.println("Thread " + record.key() + " ended");
                }
                else {
                    ListenableFuture<SendResult<String, String>> future = kafkaTemplate
                            .send("hospital_to_hospital2_doctorsList", record.key(), "error");
                    System.out.println("Response with uuid = " + record.key() + " transmit...");
                    future.addCallback(System.out::println, System.err::println);
                    kafkaTemplate.flush();
                }
            }
        }, record.key());
        newThread.start();
        System.out.println("Thread " + record.key() + " started");

    }
}
