package hosp.model.entity;


import hosp.model.enums.PatientStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotEmpty(message = "Имя не может быть нулевой длины")
    @Size(min = 2, max = 20, message = "От 2х до 20 символов")
    private String firstName;
    @NotEmpty(message = "Фамилия не может быть нулевой длины")
    @Size(min = 2, max = 20, message = "От 2х до 20 символов")
    private String secondName;
    @NotEmpty(message = "Введите номер страхования")
    private String insurance;
    @ManyToOne(fetch = FetchType.EAGER)
    private Employee doctor;
    @NotEmpty(message = "Укажите диагноз")
    @Size(min = 2, max = 60, message = "От 2х до 60 символов")
    private String diagnosis;
    @Enumerated(EnumType.STRING)
    private PatientStatus status;


}
