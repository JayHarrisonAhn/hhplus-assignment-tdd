package com.example.hhplus_arch_jvm.application.repository;

import com.example.hhplus_arch_jvm.application.domain.CourseInfo;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public interface CourseInfoRepository {

    CourseInfo createCourse(CourseInfo courseInfo);
    List<CourseInfo> findAllByDateEqualsAndRegistrationCount(LocalDate date);
}
