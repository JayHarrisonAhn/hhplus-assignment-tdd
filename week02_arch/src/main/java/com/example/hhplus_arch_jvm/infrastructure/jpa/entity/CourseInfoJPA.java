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
        courseInfoJPA.setId(domain.id());
        courseInfoJPA.setName(domain.name());
        courseInfoJPA.setDate(domain.date());
        courseInfoJPA.setDescription(domain.description());
        return courseInfoJPA;
    }
}
