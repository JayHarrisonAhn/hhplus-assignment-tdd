package com.example.hhplus_arch_jvm.infrastructure.repository;

import com.example.hhplus_arch_jvm.application.domain.CourseRegistrationCount;
import com.example.hhplus_arch_jvm.application.repository.CourseRegistrationCountRepository;
import com.example.hhplus_arch_jvm.infrastructure.jpa.CourseRegistrationCountJPARepository;
import com.example.hhplus_arch_jvm.infrastructure.jpa.entity.CourseRegistrationCountJPA;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Primary
@Component
@RequiredArgsConstructor
public class CourseRegistrationCountRepositoryImpl implements CourseRegistrationCountRepository {

    private CourseRegistrationCountJPARepository courseRegistrationCountJPARepository;

    @Override
    public CourseRegistrationCount save(CourseRegistrationCount courseRegistrationCount) {
        CourseRegistrationCountJPA courseRegistrationCountJPA = CourseRegistrationCountJPA
                .fromDomain(courseRegistrationCount);

        return this.courseRegistrationCountJPARepository
                .save(courseRegistrationCountJPA)
                .toDomain();
    }

    @Override
    public CourseRegistrationCount find(Long courseId) {
        return this.courseRegistrationCountJPARepository
                .findByCourseIdIs(courseId)
                .toDomain();
    }
}
