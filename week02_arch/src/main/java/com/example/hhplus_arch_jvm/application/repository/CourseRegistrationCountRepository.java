package com.example.hhplus_arch_jvm.application.repository;

import com.example.hhplus_arch_jvm.application.domain.CourseRegistrationCount;
import org.springframework.stereotype.Component;

@Component
public interface CourseRegistrationCountRepository {

    CourseRegistrationCount saveCourseRegistrationCount(CourseRegistrationCount courseRegistrationCount);
    CourseRegistrationCount findCourseRegistrationCount(Long courseId);
}
