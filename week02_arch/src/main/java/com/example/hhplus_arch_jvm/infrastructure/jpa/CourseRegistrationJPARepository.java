package com.example.hhplus_arch_jvm.infrastructure.jpa;

import com.example.hhplus_arch_jvm.infrastructure.jpa.entity.CourseRegistrationJPA;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CourseRegistrationJPARepository extends JpaRepository<CourseRegistrationJPA, Long> {

    Optional<CourseRegistrationJPA> findByCourseIdEqualsAndStudentIdEquals(Long courseId, Long studentId);
}
