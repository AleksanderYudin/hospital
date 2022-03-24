package hosp.model.entity;

import hosp.model.enums.TreatmentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.sql.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Prescription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    private Patient patient;
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Укажите тип лечения")
    private TreatmentType treatmentType;
    @NotEmpty(message = "Укажите метод лечения")
    @Size(min = 2, max = 100, message = "От 2х до 100 символов")
    private String treatmentDescription;
    @NotNull(message = "Укажите дату начала лечения")
    private Date startTreatment;
    @NotNull(message = "Укажите дату конца лечения")
    @Future(message = "Невалидная дата конца лечения")
    private Date endTreatment;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "treatment_time", joinColumns = @JoinColumn(name = "prescription_id"))
    @NotEmpty(message = "Укажите время лечения")
    private List<String> treatmentTime;
    @NotEmpty(message = "Укажите дозу")
    @Size(min = 1, max = 30, message = "От 1х до 30 символов")
    private String dose;
    @ManyToOne(fetch = FetchType.EAGER)
    private Employee doctor;

}
