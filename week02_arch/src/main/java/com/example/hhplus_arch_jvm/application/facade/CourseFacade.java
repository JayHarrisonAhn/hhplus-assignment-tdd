package com.example.hhplus_arch_jvm.application.facade;

import com.example.hhplus_arch_jvm.application.command.RegisterCourseCommand;
import com.example.hhplus_arch_jvm.application.command.ViewRegisteredCourseCommand;
import com.example.hhplus_arch_jvm.application.domain.Course;
import com.example.hhplus_arch_jvm.application.domain.CourseRegistration;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class CourseFacade {

    public List<Course> viewAvailableCourses(LocalDate date) {
        return null;
    }

    public CourseRegistration registerCourse(RegisterCourseCommand command) {
        return null;
    }

    public List<CourseRegistration> viewRegisteredCourses(ViewRegisteredCourseCommand command) {
        return null;
    }
}
