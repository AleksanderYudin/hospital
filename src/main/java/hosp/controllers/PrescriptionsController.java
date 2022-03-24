package hosp.controllers;

import hosp.service.EventService;
import hosp.service.PatientService;
import hosp.service.PrescriptionService;
import hosp.model.entity.Employee;
import hosp.model.entity.Prescription;
import hosp.model.enums.EventStatus;
import hosp.model.enums.PatientStatus;
import hosp.model.enums.TreatmentTime;
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
public class PrescriptionsController {

    private final PatientService patientService;
    private final PrescriptionService prescriptionService;
    private final EventService eventService;

    public PrescriptionsController(PatientService patientService, PrescriptionService prescriptionService,
                                   EventService eventService) {
        this.patientService = patientService;
        this.prescriptionService = prescriptionService;
        this.eventService = eventService;
    }


    @GetMapping("/prescriptions")
    public String getAllPrescriptions(Model model) {
        List<Prescription> prescriptionsList = prescriptionService.getAll();
        model.addAttribute("prescriptions", prescriptionsList);
        return "/doctor/prescriptions";
    }

    @GetMapping("/prescriptions/patients/{id}")
    public String getPrescriptions(@PathVariable("id") long id, Model model) {
        model.addAttribute("prescriptions", prescriptionService.getAllByPatientId(id));
        return "/doctor/prescriptions_id";
    }


    @GetMapping("/prescriptions/patients/{id}/new")
    public String newPrescription(@PathVariable("id") long id, Model model) {
        model.addAttribute("prescription", Prescription.builder().patient(patientService.get(id)).build());
        model.addAttribute("timeList", TreatmentTime.getTimeList());
        return "/doctor/add_prescription";
    }

    @Transactional
    @PostMapping("/prescriptions")
    public String addPrescription(Model model,
                                  @ModelAttribute("prescription") @Valid Prescription prescription,
                                  BindingResult bindingResult,
                                  @AuthenticationPrincipal Employee doctor) {
        if(bindingResult.hasErrors()) {
            model.addAttribute("timeList", TreatmentTime.getTimeList());
            return "/doctor/add_prescription";
        }
        prescription.setDoctor(doctor);
        patientService.changePatientStatus(PatientStatus.ON_TREATMENT.toString(), prescription.getPatient().getId());
        patientService.editDoctorId(doctor.getId(), prescription.getPatient().getId());
        eventService.save(prescriptionService.save(prescription));
        return "redirect:/doctor/prescriptions";
    }

    @Transactional
    @DeleteMapping("/prescriptions/{id}")
    public String deletePrescription(@PathVariable("id") long id) {
        eventService.deleteByPrescriptionId(id);
        prescriptionService.deleteById(id);
        return "redirect:/doctor/prescriptions";
    }


    @GetMapping("/prescriptions/edit/{id}")
    public String editPrescription(@PathVariable("id") Long id, Model model){
        model.addAttribute(prescriptionService.getPrescription(id));
        model.addAttribute("timeList", TreatmentTime.getTimeList());
        return "/doctor/edit_prescription";
    }

    @Transactional
    @PatchMapping("/prescriptions/edit")
    public String changePrescription(Model model,
                                     @ModelAttribute("prescription") @Valid Prescription prescription,
                                     BindingResult bindingResult,
                                     @AuthenticationPrincipal Employee doctor){
        if(bindingResult.hasErrors()) {
            model.addAttribute("timeList", TreatmentTime.getTimeList());
            return "/doctor/edit_prescription";
        }
        prescription.setDoctor(doctor);
        patientService.editDoctorId(doctor.getId(), prescription.getPatient().getId());
        eventService.deleteByPrescriptionId(prescription.getId());
        prescriptionService.save(prescription);
        eventService.save(prescription);
        return "redirect:/doctor/prescriptions";
    }

    @Transactional
    @PatchMapping("/prescriptions/edit/{id}")
    public String cancelPrescription(@PathVariable("id") Long id,
                                     @AuthenticationPrincipal Employee doctor){
        eventService.toCancelledEventStatus(EventStatus.CANCELLED.toString(), id);
        eventService.updateCommentByPrescriptionId("Назначение отменено, " +
                "врач " + doctor.getSecondName() + " " + doctor.getFirstName(), id);
        return "redirect:/doctor/prescriptions";
    }


}
