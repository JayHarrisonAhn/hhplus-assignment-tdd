package com.example.hhplus_arch_jvm.application.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Builder
@Getter @Setter
public class CourseRegistration {
    Long id;
    LocalDate date;
    Long coachId;
    Long studentId;
}
