package com.example.hhplus_arch_jvm.interfaces.api;

import com.example.hhplus_arch_jvm.interfaces.dto.CourseControllerDto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/course")
public class CourseController {

    @GetMapping("/register")
    public GetAllAvailableCourses.Response getAllAvailableCourses(
            GetAllAvailableCourses.Request request
    ) {
        return null;
    }

    @PostMapping("/register")
    public RegisterCourse.Response registerCourse(
            RegisterCourse.Request request
    ) {
        return null;
    }

    @GetMapping("/register/user/:userId")
    public GetRegisteredCourses.Response getRegisteredCourses(
            GetRegisteredCourses.Request request
    ) {
        return null;
    }
}
