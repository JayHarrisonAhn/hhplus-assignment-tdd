package com.example.hhplus_arch_jvm.infrastructure.jpa;

import com.example.hhplus_arch_jvm.infrastructure.jpa.entity.CourseRegistrationCountJPA;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

public interface CourseRegistrationCountJPARepository extends JpaRepository<CourseRegistrationCountJPA, Long> {

    @Lock(LockModeType.PESSIMISTIC_READ)
    CourseRegistrationCountJPA findByCourseIdIs(Long courseId);
}
