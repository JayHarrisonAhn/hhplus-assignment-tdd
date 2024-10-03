package com.example.hhplus_arch_jvm.infrastructure.repository;

import com.example.hhplus_arch_jvm.application.domain.CourseRegistration;
import com.example.hhplus_arch_jvm.application.domain.CourseRegistrationInfo;
import com.example.hhplus_arch_jvm.application.repository.CourseRegistrationRepository;
import com.example.hhplus_arch_jvm.infrastructure.jpa.CourseInfoJPARepository;
import com.example.hhplus_arch_jvm.infrastructure.jpa.CourseRegistrationJPARepository;
import com.example.hhplus_arch_jvm.infrastructure.jpa.entity.CourseRegistrationJPA;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.List;

@Primary
@Component
@RequiredArgsConstructor
public class CourseRegistrationRepositoryImpl implements CourseRegistrationRepository {

    private CourseInfoJPARepository courseInfoJPARepository;
    private CourseRegistrationJPARepository courseRegistrationJPARepository;

    @Override
    public CourseRegistration save(CourseRegistration courseRegistration) {
        CourseRegistrationJPA course = courseRegistrationJPARepository.save(
                CourseRegistrationJPA.fromDomain(courseRegistration)
        );
        return course.toDomain();
    }
}
