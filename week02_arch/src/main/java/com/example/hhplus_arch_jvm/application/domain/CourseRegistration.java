package com.example.hhplus_arch_jvm.application.domain;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record CourseRegistration(
        Long id,
        LocalDate date,
        Long coachId,
        Long studentId
) {
}
