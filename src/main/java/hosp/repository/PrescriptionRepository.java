package hosp.repository;

import hosp.model.entity.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {

    void deleteByPatientId(Long id);

    List<Prescription> findAllByPatientId(Long id);

}
