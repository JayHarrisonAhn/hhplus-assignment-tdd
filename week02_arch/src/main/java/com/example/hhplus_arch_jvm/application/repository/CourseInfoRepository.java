package com.example.hhplus_arch_jvm.application.repository;

import com.example.hhplus_arch_jvm.application.domain.CourseInfo;
import com.example.hhplus_arch_jvm.application.domain.CourseRegistrationInfo;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public interface CourseInfoRepository {

    CourseInfo save(CourseInfo courseInfo);
    List<CourseInfo> findAllRegistrableByDate(LocalDate date);
    List<CourseRegistrationInfo> findAllRegisteredByStudentId(Long studentId);
}
