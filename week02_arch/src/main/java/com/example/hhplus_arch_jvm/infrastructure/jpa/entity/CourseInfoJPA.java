package com.example.hhplus_arch_jvm.infrastructure.jpa.entity;

import com.example.hhplus_arch_jvm.application.domain.CourseInfo;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter @Setter
public class CourseInfoJPA {

    @Id @GeneratedValue
    Long id;

    String name;

    LocalDate date;

    String description;

    public CourseInfo toDomain() {
        return CourseInfo.builder()
                .id(id)
                .name(name)
                .date(date)
                .description(description)
                .build();
    }

    static public CourseInfoJPA fromDomain(CourseInfo domain) {
        CourseInfoJPA courseInfoJPA = new CourseInfoJPA();
        courseInfoJPA.setId(domain.getId());
        courseInfoJPA.setName(domain.getName());
        courseInfoJPA.setDate(domain.getDate());
        courseInfoJPA.setDescription(domain.getDescription());
        return courseInfoJPA;
    }
}
