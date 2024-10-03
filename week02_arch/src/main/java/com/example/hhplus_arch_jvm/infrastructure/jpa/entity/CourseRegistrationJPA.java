package com.example.hhplus_arch_jvm.infrastructure.jpa.entity;

import com.example.hhplus_arch_jvm.application.domain.CourseRegistration;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(
        uniqueConstraints = {
            @UniqueConstraint(
                    name = "",
                    columnNames = {"courseId", "studentId"}
            )
        }
)
@Getter @Setter
public class CourseRegistrationJPA {

    @Id @GeneratedValue
    private Long courseId;

    private Long studentId;

    private LocalDateTime createdAt;

    public CourseRegistration toDomain() {
        return CourseRegistration.builder()
                .courseId(courseId)
                .studentId(studentId)
                .createdAt(createdAt)
                .build();
    }

    static public CourseRegistrationJPA fromDomain(CourseRegistration domain) {
        CourseRegistrationJPA courseRegistration = new CourseRegistrationJPA();
        courseRegistration.setCourseId(domain.getCourseId());
        courseRegistration.setStudentId(domain.getStudentId());
        courseRegistration.setCreatedAt(domain.getCreatedAt());
        return courseRegistration;
    }
}
