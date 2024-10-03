package com.example.hhplus_arch_jvm.infrastructure.repository;

import com.example.hhplus_arch_jvm.application.domain.CourseRegistration;
import com.example.hhplus_arch_jvm.application.repository.CourseRegistrationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Primary
@Component
@RequiredArgsConstructor
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
