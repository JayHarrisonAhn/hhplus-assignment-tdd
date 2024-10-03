package com.example.hhplus_arch_jvm.infrastructure.repository;

import com.example.hhplus_arch_jvm.application.domain.CourseRegistration;
import com.example.hhplus_arch_jvm.application.repository.CourseRegistrationRepository;

import java.time.LocalDate;
import java.util.List;

public class CourseRegistrationRepositoryImpl implements CourseRegistrationRepository {
    @Override
    public CourseRegistration createCourseRegistration(CourseRegistration courseRegistration) {
        return null;
    }

    @Override
    public List<CourseRegistration> findAllCourseRegistrationByUserIdAndDate(String userId, LocalDate date) {
        return List.of();
    }
}
