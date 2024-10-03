package com.example.hhplus_arch_jvm.application.facade;

import com.example.hhplus_arch_jvm.application.command.RegisterCourseCommand;
import com.example.hhplus_arch_jvm.application.command.ViewRegisteredCourseCommand;
import com.example.hhplus_arch_jvm.application.domain.CourseInfo;
import com.example.hhplus_arch_jvm.application.domain.CourseRegistration;
import com.example.hhplus_arch_jvm.application.domain.CourseRegistrationInfo;
import com.example.hhplus_arch_jvm.application.service.CourseService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@Transactional
@RequiredArgsConstructor
public class CourseFacade {

    private final CourseService courseService;

    public List<CourseInfo> viewAvailableCourses(LocalDate date) {
        return courseService.getAvailableCourses(date);
    }

    public CourseRegistration registerCourse(RegisterCourseCommand command) {
        courseService.decrementRegistrationCount(command.courseId());
        return courseService.addCourseRegistration(command.courseId(), command.userId());
    }

    public List<CourseRegistrationInfo> viewRegisteredCourses(ViewRegisteredCourseCommand command) {
        return courseService.getRegisteredCourses(command.studentId());
    }
}
