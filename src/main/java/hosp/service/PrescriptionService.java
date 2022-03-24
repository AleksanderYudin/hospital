package hosp.service;

import hosp.model.entity.Prescription;
import hosp.repository.PrescriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class PrescriptionService {

    @Autowired
    private PrescriptionRepository repository;


    public List<Prescription> getAll() {
        List<Prescription> prescriptionList = repository.findAll();
        prescriptionList.sort((a, b) -> a.getPatient().getSecondName().compareTo(b.getPatient().getSecondName()));
        return prescriptionList;
    }

    public List<Prescription> getAllByPatientId(Long id) {
        return repository.findAllByPatientId(id);
    }

    public Prescription getPrescription(long id) {
        Optional<Prescription> optional = repository.findById(id);
        if(optional.isPresent())
            return optional.get();
        return null;
    }

    public Prescription save(Prescription prescription) {
        return repository.save(prescription);

    }

    public void deleteById(Long prescription_id) {
        repository.deleteById(prescription_id);
    }

    public void deleteByPatientId(Long patient_id){
        repository.deleteByPatientId(patient_id);
    }

}
