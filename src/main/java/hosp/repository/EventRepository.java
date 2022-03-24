package hosp.repository;

import hosp.model.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findAllByPatientIdOrderByDateTime(long id);

    void deleteByPrescriptionId(long id);

    void deleteByPatientId(Long id);

    List<Event> findByDateTimeAfter(LocalDateTime date);

    List<Event> findByDateTimeBefore(LocalDateTime date);

    @Query(value = "SELECT * FROM event WHERE date_time BETWEEN ?1 AND ?2", nativeQuery = true)
    List<Event> findByFilterDate(LocalDateTime t1, LocalDateTime t2);

    @Query(value = "SELECT * FROM event ORDER BY id", nativeQuery = true)
    List<Event> findAllOrderById();

    @Query(value = "SELECT * FROM event ORDER BY date_time", nativeQuery = true)
    List<Event> findAllOrderByDate();


    @Modifying
    @Query(value = "UPDATE event SET event_status = ?1 WHERE patient_id = ?2 AND date_time > now() " +
            "AND event_status != 'CANCELLED'", nativeQuery = true)
    void updateEventStatusByPatientId(String eventStatus, Long id);
    @Modifying
    @Query(value = "UPDATE event SET comment = ?1 WHERE patient_id = ?2 AND date_time > now() " +
            "AND event_status != 'CANCELLED'", nativeQuery = true)
    void updateCommentByPatientId(String comment, Long id);


    @Modifying
    @Query(value = "UPDATE event SET event_status = ?1 WHERE prescription_id = ?2 AND date_time > now()", nativeQuery = true)
    void updateEventStatusByPrescriptionId(String eventStatus, Long id);

    @Modifying
    @Query(value = "UPDATE event SET comment = ?1 WHERE prescription_id = ?2 AND date_time > now()", nativeQuery = true)
    void updateCommentByPrescriptionId(String comment, Long id);



    @Modifying
    @Query(value = "UPDATE event SET event_status = ?1, comment = ?2 WHERE id = ?3", nativeQuery = true)
    void updateEventNurse(String status, String comment, Long id);

}
