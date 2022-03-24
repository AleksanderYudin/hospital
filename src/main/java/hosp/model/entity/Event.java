package hosp.model.entity;

import hosp.model.enums.EventStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Patient patient;
    private LocalDateTime dateTime;
    @ManyToOne(cascade = CascadeType.MERGE)
    private Prescription prescription;
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Укажите статус события")
    private EventStatus eventStatus;
    private String comment;


}
