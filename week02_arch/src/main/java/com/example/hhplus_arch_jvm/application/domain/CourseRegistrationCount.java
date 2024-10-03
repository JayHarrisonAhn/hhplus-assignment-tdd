package com.example.hhplus_arch_jvm.application.domain;

import lombok.Builder;

@Builder
public record CourseRegistrationCount(
        Long courseId,
        Integer count,
        Integer max
) {
}
