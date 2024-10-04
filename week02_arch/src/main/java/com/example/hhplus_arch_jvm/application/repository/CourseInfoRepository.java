package com.example.hhplus_arch_jvm.application.repository;

import com.example.hhplus_arch_jvm.application.domain.CourseInfo;
import com.example.hhplus_arch_jvm.application.domain.CourseRegistrationInfo;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
public interface CourseInfoRepository {

    CourseInfo save(CourseInfo courseInfo);
    Optional<CourseInfo> find(Long courseId);
    List<CourseInfo> findAllRegistrableByDate(LocalDate date);
    List<CourseRegistrationInfo> findAllRegisteredByStudentId(Long studentId);
}
