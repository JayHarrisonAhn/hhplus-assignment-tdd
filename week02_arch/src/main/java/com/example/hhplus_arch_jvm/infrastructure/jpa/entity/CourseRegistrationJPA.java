package com.example.hhplus_arch_jvm.infrastructure.jpa.entity;

import com.example.hhplus_arch_jvm.application.domain.CourseRegistration;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

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

    private Long studentId;

    private Date createdAt;

    public CourseRegistration toDomain() {
        return CourseRegistration.builder()
                .courseId(id)
                .studentId(studentId)
                .createdAt(createdAt)
                .build();
    }

    static public CourseRegistrationJPA fromDomain(CourseRegistration domain) {
        CourseRegistrationJPA courseRegistration = new CourseRegistrationJPA();
        courseRegistration.setId(domain.getCourseId());
        courseRegistration.setStudentId(domain.getStudentId());
        courseRegistration.setCreatedAt(domain.getCreatedAt());
        return courseRegistration;
    }
}
