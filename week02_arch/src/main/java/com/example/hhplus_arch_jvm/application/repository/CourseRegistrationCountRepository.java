package com.example.hhplus_arch_jvm.application.repository;

import com.example.hhplus_arch_jvm.application.domain.CourseRegistrationCount;
import org.springframework.stereotype.Component;

@Component
public interface CourseRegistrationCountRepository {

    CourseRegistrationCount save(CourseRegistrationCount courseRegistrationCount);
    CourseRegistrationCount find(Long courseId);
}
