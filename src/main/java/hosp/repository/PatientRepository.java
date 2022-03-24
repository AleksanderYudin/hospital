package hosp.repository;

import hosp.model.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    @Modifying
    @Query(value = "UPDATE patient SET status = ?1 WHERE id = ?2", nativeQuery = true)
    void updatePatientStatus(String status, Long id);

    @Modifying
    @Query(value = "UPDATE patient SET doctor_id = ?1 WHERE id = ?2", nativeQuery = true)
    void updateDoctorId(Long doctor_id, Long patient_id);

    @Query(value = "SELECT * FROM patient ORDER BY second_name", nativeQuery = true)
    List<Patient> findAllOrderBySecondName();

}
