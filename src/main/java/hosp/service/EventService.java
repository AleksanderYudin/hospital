package hosp.service;

import hosp.model.entity.Event;
import hosp.model.entity.Prescription;
import hosp.model.enums.EventStatus;
import hosp.repository.EventRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Log4j2
@Component
public class EventService {
    @Autowired
    private EventRepository repository;

    public List<Event> getAllEvents() {
        return repository.findAllOrderByDate();
    }


    public List<Event> getFilteredEvents(String start, String end, Long id) {
        List<Event> eventList = new ArrayList<>();
        LocalDateTime startDate;
        LocalDateTime endDate;
        String datePattern = "yyyy-MM-dd'T'HH:mm";

        if(id == null && start == null && end == null) return getAllEvents();

        if(!start.isEmpty() && !end.isEmpty()) {
            startDate = LocalDateTime.parse(start, DateTimeFormatter.ofPattern(datePattern));
            endDate = LocalDateTime.parse(end, DateTimeFormatter.ofPattern(datePattern));
            eventList = repository.findByFilterDate(startDate, endDate);
        }
        if(!start.isEmpty() && end.isEmpty()) {
            startDate = LocalDateTime.parse(start, DateTimeFormatter.ofPattern(datePattern));
            eventList = repository.findByDateTimeAfter(startDate);
        }
        if(start.isEmpty() && !end.isEmpty()) {
            endDate = LocalDateTime.parse(end, DateTimeFormatter.ofPattern(datePattern));
            eventList = repository.findByDateTimeBefore(endDate);
        }
        if(!eventList.isEmpty()) {
            eventList.sort((a, b) -> a.getDateTime().compareTo(b.getDateTime()));
        }
        if(id != null && !eventList.isEmpty()) {
            return eventList.stream()
                    .filter(event -> event.getPatient().getId() == id)
                    .collect(Collectors.toList());
        }
        if(id != null && eventList.isEmpty()) {
            return repository.findAllByPatientIdOrderByDateTime(id);
        }
        else return eventList;
    }


    public Event getEvent(long id) {
        Optional<Event> optional = repository.findById(id);
        if(optional.isPresent())
            return optional.get();
        return null;
    }

    @Transactional
    public void editEvent(String status, String comment, Long id) {
        repository.updateEventNurse(status, comment, id);
    }

    public void deleteByPrescriptionId(Long prescription_id){
        repository.deleteByPrescriptionId(prescription_id);
    }

    public void deleteByPatientId(Long patient_id){
        repository.deleteByPatientId(patient_id);
    }

    public void toCancelledEventStatus(String eventStatus, Long id) {
        repository.updateEventStatusByPrescriptionId(eventStatus, id);
    }

    public void updateCommentByPrescriptionId(String comment, Long id){
        repository.updateCommentByPrescriptionId(comment, id);
    }

    public void updateCommentByPatientId(String comment, Long id){
        repository.updateCommentByPatientId(comment, id);
    }


    public void save(Prescription prescription) {
        LocalDate start = prescription.getStartTreatment().toLocalDate();
        LocalDate end = prescription.getEndTreatment().toLocalDate();
        List<String> timeList = prescription.getTreatmentTime();
        Date date = new Date();
        do {
            for(String time : timeList) {
                date = timeParser(time);
                Event newEvent = Event.builder()
                        .patient(prescription.getPatient())
                        .prescription(prescription)
                        .eventStatus(EventStatus.PLANNED)
                        .dateTime(LocalDateTime.of(start, LocalTime.of(date.getHours(), date.getMinutes())))
                        .build();
                repository.save(newEvent);
            }
            start = start.plusDays(1);
        } while (start.isBefore(end.plusDays(1)));
    }


    public Date timeParser(String str) {
        Date time = null;
        try {
            time = new SimpleDateFormat("HH:mm").parse(str);
        } catch (ParseException e) {
            log.error(e);
        }
    return time;
    }

}
