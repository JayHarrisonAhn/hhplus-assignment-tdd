package com.example.hhplus_arch_jvm.application.repository;

import com.example.hhplus_arch_jvm.application.domain.CourseRegistration;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public interface CourseRegistrationRepository {

    CourseRegistration save(CourseRegistration courseRegistration);
    Optional<CourseRegistration> find(Long courseId, Long studentId);
}
