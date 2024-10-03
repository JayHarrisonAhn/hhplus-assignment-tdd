package com.example.hhplus_arch_jvm.application.domain;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record CourseInfo(
        Long id,
        String name,
        LocalDate date,
        String description
) {
}
