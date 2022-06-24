package hosp.repository;

import hosp.model.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {


    @Modifying
    @Query(value = "insert into appointment(doctor_id, date_time, is_vacant)\n" +
            " values(?1, ?2, ?3) IF NOT EXISTS (SELECT * FROM appointment WHERE doctor_id = ?1 AND" +
            "date_time = ?2", nativeQuery = true)
    boolean saveAppointments(Long id, LocalDateTime dateTime, boolean isVacant);

}
