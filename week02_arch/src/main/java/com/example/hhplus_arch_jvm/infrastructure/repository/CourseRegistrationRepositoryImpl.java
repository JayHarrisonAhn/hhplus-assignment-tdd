package com.example.hhplus_arch_jvm.infrastructure.repository;

import com.example.hhplus_arch_jvm.application.domain.CourseRegistration;
import com.example.hhplus_arch_jvm.application.repository.CourseRegistrationRepository;
import com.example.hhplus_arch_jvm.infrastructure.jpa.CourseRegistrationJPARepository;
import com.example.hhplus_arch_jvm.infrastructure.jpa.entity.CourseRegistrationJPA;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Primary
@Component
@AllArgsConstructor
public class CourseRegistrationRepositoryImpl implements CourseRegistrationRepository {

    private CourseRegistrationJPARepository courseRegistrationJPARepository;

    @Override
    public CourseRegistration save(CourseRegistration courseRegistration) {
        CourseRegistrationJPA course = courseRegistrationJPARepository.save(
                CourseRegistrationJPA.fromDomain(courseRegistration)
        );
        return course.toDomain();
    }

    @Override
    public Optional<CourseRegistration> find(Long courseId, Long studentId) {
        Optional<CourseRegistrationJPA> course = courseRegistrationJPARepository
                .findByCourseIdEqualsAndStudentIdEquals(courseId, studentId);
        return course.map(CourseRegistrationJPA::toDomain);
    }
}
