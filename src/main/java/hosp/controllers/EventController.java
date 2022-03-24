package hosp.controllers;

import hosp.service.EventService;
import hosp.model.entity.Employee;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/nurse")
@PreAuthorize("hasAuthority('NURSE')")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }


    @GetMapping("/events")
    public String getAllEvents(Model model,
                               @RequestParam(value = "patient_id", required = false) Long id,
                               @RequestParam(value = "startDate", required = false) String startDate,
                               @RequestParam(value = "endDate", required = false) String endDate) {
        model.addAttribute("eventList", eventService.getFilteredEvents(startDate, endDate, id));
        model.addAttribute("nowTime", LocalDateTime.now().plusHours(12));
        return "/nurse/events";
    }

    @GetMapping("/events/edit/{id}")
    public String editEventPage(@PathVariable("id") long id, Model model,
                                @AuthenticationPrincipal Employee nurse){
        model.addAttribute("name", new String(nurse.getSecondName() + " " + nurse.getFirstName()));
        model.addAttribute("event", eventService.getEvent(id));
        return "/nurse/edit_events";
    }

    @PatchMapping("/events/edit/{id}")
    public String editEvent(@ModelAttribute("status") String status,
                            @ModelAttribute("comment") String comment,
                            @PathVariable Long id){
        eventService.editEvent(status, comment, id);
        return "redirect:/nurse/events";
    }

}
