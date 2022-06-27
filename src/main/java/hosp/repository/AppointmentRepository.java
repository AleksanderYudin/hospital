package hosp.repository;

import hosp.model.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    @Query(value = "insert into appointment(doctor_id, date_time, is_vacant) SELECT ?1, ?2, ?3" +
            " WHERE NOT EXISTS (SELECT id FROM appointment WHERE doctor_id = ?1 AND" +
            " date_time = ?2)", nativeQuery = true)
//@Query(value = "IF NOT EXISTS (SELECT * FROM appointment WHERE doctor_id = ?1 AND" +
//        " date_time = ?2) BEGIN" +
//        " insert into appointment(doctor_id, date_time, is_vacant)" +
//        " values(?1, ?2, ?3)" +
//        " END", nativeQuery = true)
//@Query(value = "insert into appointment(doctor_id, date_time, is_vacant) values(?1, ?2, ?3)" +
//        " WHERE COUNT(SELECT * FROM appointment WHERE doctor_id = ?1 AND date_time = ?2) !=0", nativeQuery = true)
//    @Query(value = "insert into appointment(doctor_id, date_time, is_vacant) values(?1, ?2, ?3)", nativeQuery = true)
    Appointment saveAppointment(Long doctor_id, LocalDateTime dateTime, boolean isVacant);

    List<Appointment> findAllByDoctorId(Long id);
}
