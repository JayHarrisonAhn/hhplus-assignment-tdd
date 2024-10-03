package com.example.hhplus_arch_jvm.infrastructure.repository;

import com.example.hhplus_arch_jvm.application.domain.Course;
import com.example.hhplus_arch_jvm.application.domain.CourseRegistration;
import com.example.hhplus_arch_jvm.application.domain.CourseRegistrationCount;
import com.example.hhplus_arch_jvm.application.repository.CourseRepository;
import com.example.hhplus_arch_jvm.infrastructure.jpa.CourseJPARepository;
import com.example.hhplus_arch_jvm.infrastructure.jpa.CourseRegistrationCountJPARepository;
import com.example.hhplus_arch_jvm.infrastructure.jpa.CourseRegistrationJPARepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Primary
@Component
@RequiredArgsConstructor
public class CourseRepositoryImpl implements CourseRepository {

    private final CourseJPARepository courseJPARepository;
    private final CourseRegistrationJPARepository courseRegistrationJPARepository;
    private final CourseRegistrationCountJPARepository courseRegistrationCountJPARepository;

    @Override
    public Course createCourse(Course course) {
        return null;
    }

    @Override
    public List<Course> findAllCourseByRegistrationCountLessThanRegistrationMaxAndDateEquals(LocalDate date) {
        return List.of();
    }

    @Override
    public CourseRegistrationCount createCourseRegistrationCount(CourseRegistrationCount courseRegistrationCount) {
        return null;
    }

    @Override
    public CourseRegistrationCount findCourseRegistrationCount(CourseRegistrationCount courseRegistrationCount) {
        return null;
    }

    @Override
    public CourseRegistrationCount updateCourseRegistrationCount(CourseRegistrationCount courseRegistrationCount) {
        return null;
    }

    @Override
    public CourseRegistration createCourseRegistration(CourseRegistration courseRegistration) {
        return null;
    }

    @Override
    public List<CourseRegistration> findAllCourseRegistrationByUserIdAndDate(String userId, LocalDate date) {
        return List.of();
    }
}
