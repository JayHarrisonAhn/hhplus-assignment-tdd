package com.example.hhplus_arch_jvm.application.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;

@SuperBuilder
@Getter @Setter
@ToString
public class CourseRegistrationInfo extends CourseInfo {
    public CourseRegistrationInfo(
            Long id,
            String name,
            LocalDate date,
            String description,
            LocalDateTime registeredAt
    ) {
        super(id, name, date, description);
        this.registeredAt = registeredAt;
    }

    LocalDateTime registeredAt;
}
