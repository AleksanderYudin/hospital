package hosp.service;

import hosp.model.entity.Employee;
import hosp.model.entity.Patient;
import hosp.model.enums.EventStatus;
import hosp.model.enums.PatientStatus;
import hosp.repository.EventRepository;
import hosp.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
public class PatientService {

    @Autowired
    private PatientRepository repository;
    @Autowired
    private EventRepository eventRepository;

    public List<Patient> getAll() {
        return repository.findAllOrderBySecondName();
    }

    public Patient get(long id) {
        Optional<Patient> patientOptional = repository.findById(id);
        if(patientOptional.isPresent())
            return patientOptional.get();
        return null;
    }

    public void save(Patient patient) {
        repository.save(patient);
    }

    @Transactional
    public void discharge(long id, Employee doctor) {
            Patient patient = get(id);
            patient.setStatus(PatientStatus.HEALTHY);
            eventRepository.updateEventStatusByPatientId(EventStatus.DONE.toString(), id);
            eventRepository.updateCommentByPatientId("Досрочное прекращение процедур, врач " +
                    doctor.getSecondName() + " " + doctor.getFirstName(), id);
            repository.save(patient);
    }

    public void delete(long id) {
        repository.deleteById(id);
    }

    public void edit(Patient patient) {
        repository.save(patient);
    }

    public void editDoctorId(Long doctor_id, Long patient_id){
        repository.updateDoctorId(doctor_id, patient_id);
    }

    public void changePatientStatus(String status, Long patient_id) {
        repository.updatePatientStatus(status, patient_id);
    }
}
