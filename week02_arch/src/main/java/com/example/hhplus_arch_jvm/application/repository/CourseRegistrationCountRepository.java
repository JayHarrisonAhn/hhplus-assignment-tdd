package com.example.hhplus_arch_jvm.application.repository;

import com.example.hhplus_arch_jvm.application.domain.CourseRegistrationCount;
import org.springframework.stereotype.Component;

@Component
public interface CourseRegistrationCountRepository {

    CourseRegistrationCount createCourseRegistrationCount(CourseRegistrationCount courseRegistrationCount);
    CourseRegistrationCount findCourseRegistrationCount(CourseRegistrationCount courseRegistrationCount);
    CourseRegistrationCount updateCourseRegistrationCount(CourseRegistrationCount courseRegistrationCount);
}
