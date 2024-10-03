package com.example.hhplus_arch_jvm.application.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Builder
@Getter @Setter
public class CourseRegistration {
    Long courseId;
    Long studentId;
    Date createdAt;
}
