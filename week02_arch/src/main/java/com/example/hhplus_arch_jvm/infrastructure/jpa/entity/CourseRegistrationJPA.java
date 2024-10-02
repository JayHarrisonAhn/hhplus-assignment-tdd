package com.example.hhplus_arch_jvm.infrastructure.jpa.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDate;

@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(
                name = "",
                columnNames = {"date", "coachId", "studentId"}
        )
})
@Getter
public class CourseRegistrationJPA {

    @Id @GeneratedValue
    private Long id;

    private LocalDate date;

    private Long coachId;

    private Long studentId;
}
