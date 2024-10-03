package com.example.hhplus_arch_jvm.application.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter @Setter
public class CourseRegistrationCount {
    Long courseId;
    Integer count;
    Integer max;
}
