package com.example.hhplus_arch_jvm.application.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Builder
@Getter @Setter
public class CourseInfo {
    Long id;
    String name;
    LocalDate date;
    String description;
}
