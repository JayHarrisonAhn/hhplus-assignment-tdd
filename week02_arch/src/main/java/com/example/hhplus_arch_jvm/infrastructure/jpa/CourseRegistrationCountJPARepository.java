package com.example.hhplus_arch_jvm.infrastructure.jpa;

import com.example.hhplus_arch_jvm.infrastructure.jpa.entity.CourseRegistrationCountJPA;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRegistrationCountJPARepository extends JpaRepository<CourseRegistrationCountJPA, Long> {

    CourseRegistrationCountJPA findByCourseIdIs(Long courseId);
}
