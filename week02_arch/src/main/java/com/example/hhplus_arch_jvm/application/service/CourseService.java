package com.example.hhplus_arch_jvm.application.service;

import com.example.hhplus_arch_jvm.application.domain.Course;
import com.example.hhplus_arch_jvm.application.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseService {
    private final CourseRepository courseRepository;

    public List<Course> getAvailableCourses() {
        return null;
    }

    public Course registerCourse(long courseId) {
        return null;
    }

    public List<Course> getRegisteredCourses(long userId) {
        return null;
    }
}
