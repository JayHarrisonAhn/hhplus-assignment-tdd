package com.example.hhplus_arch_jvm.application.repository;

import com.example.hhplus_arch_jvm.application.domain.CourseInfo;
import com.example.hhplus_arch_jvm.application.domain.CourseRegistration;
import com.example.hhplus_arch_jvm.application.domain.CourseRegistrationCount;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public interface CourseRepository {

    CourseInfo createCourse(CourseInfo courseInfo);
    List<CourseInfo> findAllCourseByRegistrationCountLessThanRegistrationMaxAndDateEquals(LocalDate date);

    CourseRegistrationCount createCourseRegistrationCount(CourseRegistrationCount courseRegistrationCount);
    CourseRegistrationCount findCourseRegistrationCount(CourseRegistrationCount courseRegistrationCount);
    CourseRegistrationCount updateCourseRegistrationCount(CourseRegistrationCount courseRegistrationCount);

    CourseRegistration createCourseRegistration(CourseRegistration courseRegistration);
    List<CourseRegistration> findAllCourseRegistrationByUserIdAndDate(String userId, LocalDate date);

}
