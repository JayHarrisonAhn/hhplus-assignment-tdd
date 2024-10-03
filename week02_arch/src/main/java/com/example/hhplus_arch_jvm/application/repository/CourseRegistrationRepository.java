package com.example.hhplus_arch_jvm.application.repository;

import com.example.hhplus_arch_jvm.application.domain.CourseRegistration;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public interface CourseRegistrationRepository {

    CourseRegistration createCourseRegistration(CourseRegistration courseRegistration);
    List<CourseRegistration> findAllCourseRegistrationByUserIdAndDate(String userId, LocalDate date);
}
