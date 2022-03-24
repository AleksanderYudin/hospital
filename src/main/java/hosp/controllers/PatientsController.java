package hosp.controllers;

import hosp.service.EventService;
import hosp.service.PatientService;
import hosp.service.PrescriptionService;
import hosp.model.entity.Employee;
import hosp.model.entity.Patient;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/doctor")
@PreAuthorize("hasAuthority('DOCTOR')")
public class PatientsController {

    private final PatientService patientService;
    private final PrescriptionService prescriptionService;
    private final EventService eventService;

    public PatientsController(PatientService patientService, PrescriptionService prescriptionService,
                              EventService eventService) {
        this.patientService = patientService;
        this.prescriptionService = prescriptionService;
        this.eventService = eventService;
    }

    @GetMapping("/patients")
    public String getAllPatients(Model model) {
        List<Patient> patientList = patientService.getAll();
        model.addAttribute("patients", patientList);
        return "/doctor/patients";
    }

    @GetMapping("/patients/new")
    public String newPatient(@ModelAttribute("patient") Patient patient) {
        return "/doctor/add_patient";
    }

    @PostMapping("/patients")
    public String createPatient(@ModelAttribute("patient") @Valid Patient patient,
                                BindingResult bindingResult,
                                @AuthenticationPrincipal Employee doctor) {
        if(bindingResult.hasErrors()) return "/doctor/add_patient";
        patient.setDoctor(doctor);
        patientService.save(patient);
        return "redirect:/doctor/patients";
    }


    @GetMapping("/patients/{id}")
    public String getPatient(@PathVariable("id") long id, Model model) {
        model.addAttribute("patient", patientService.get(id));
        return "/doctor/patient_id";
    }

    @Transactional
    @DeleteMapping("/patients/{id}")
    public String deletePatient(@PathVariable("id") long id) {
        eventService.deleteByPatientId(id);
        prescriptionService.deleteByPatientId(id);
        patientService.delete(id);
        return "redirect:/doctor/patients";
    }

    @GetMapping("/patients/edit/{id}")
    public String editPage(@PathVariable("id") long id, Model model) {
        model.addAttribute("patient", patientService.get(id));
        return "/doctor/edit_patient";
    }


    @PatchMapping("/patients/edit/{id}")
    public String editPatient(@ModelAttribute("patient") @Valid Patient patient,
                              BindingResult bindingResult,
                              @PathVariable("id") long id,
                              @AuthenticationPrincipal Employee doctor) {
        if(bindingResult.hasErrors()) return "/doctor/edit_patient";
        patient.setDoctor(doctor);
        patientService.edit(patient);
        return "redirect:/doctor/patients/{id}";
    }

    @PatchMapping("/patients/{id}")
    public String dischargePatient(@PathVariable("id") long id,
                                   @AuthenticationPrincipal Employee doctor) {
        patientService.discharge(id, doctor);
        return "redirect:/doctor/patients/{id}";
    }

}
