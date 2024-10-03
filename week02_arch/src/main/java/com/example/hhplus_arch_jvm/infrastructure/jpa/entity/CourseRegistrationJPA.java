package com.example.hhplus_arch_jvm.infrastructure.jpa.entity;

import com.example.hhplus_arch_jvm.application.domain.CourseRegistration;
import com.example.hhplus_arch_jvm.application.domain.CourseRegistrationCount;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(
        uniqueConstraints = {
            @UniqueConstraint(
                    name = "",
                    columnNames = {"date", "coachId", "studentId"}
            )
        }
)
@Getter @Setter
public class CourseRegistrationJPA {

    @Id @GeneratedValue
    private Long id;

    private LocalDate date;

    private Long coachId;

    private Long studentId;

    public CourseRegistration toDomain() {
        return CourseRegistration.builder()
                .id(id)
                .date(date)
                .coachId(coachId)
                .studentId(studentId)
                .build();
    }

    static public CourseRegistrationJPA fromDomain(CourseRegistration domain) {
        CourseRegistrationJPA courseRegistration = new CourseRegistrationJPA();
        courseRegistration.setId(domain.getId());
        courseRegistration.setDate(domain.getDate());
        courseRegistration.setCoachId(domain.getCoachId());
        courseRegistration.setStudentId(domain.getStudentId());
        return courseRegistration;
    }
}
