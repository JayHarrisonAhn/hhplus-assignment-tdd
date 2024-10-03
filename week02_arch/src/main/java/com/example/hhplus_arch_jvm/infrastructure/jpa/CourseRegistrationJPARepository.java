package com.example.hhplus_arch_jvm.infrastructure.jpa;

import com.example.hhplus_arch_jvm.infrastructure.jpa.entity.CourseRegistrationJPA;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRegistrationJPARepository extends JpaRepository<CourseRegistrationJPA, Long> {
}
