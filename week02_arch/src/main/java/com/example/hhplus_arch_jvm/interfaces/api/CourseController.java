package com.example.hhplus_arch_jvm.interfaces.api;

import com.example.hhplus_arch_jvm.application.command.RegisterCourseCommand;
import com.example.hhplus_arch_jvm.application.command.ViewRegisteredCourseCommand;
import com.example.hhplus_arch_jvm.application.domain.CourseInfo;
import com.example.hhplus_arch_jvm.application.domain.CourseRegistration;
import com.example.hhplus_arch_jvm.application.domain.CourseRegistrationInfo;
import com.example.hhplus_arch_jvm.application.facade.CourseFacade;
import com.example.hhplus_arch_jvm.interfaces.dto.CourseControllerDto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/course")
public class CourseController {

    private CourseFacade courseFacade;

    @GetMapping("/register")
    public List<GetAllAvailableCourses.Response> getAllAvailableCourses(
            GetAllAvailableCourses.Request request
    ) {
        List<CourseInfo> courses = courseFacade.viewAvailableCourses(request.date());
        return courses.stream().map(
                c -> GetAllAvailableCourses.Response.builder()
                        .id(c.getId())
                        .name(c.getName())
                        .date(c.getDate())
                        .description(c.getDescription())
                        .build()
        ).toList();
    }

    @PostMapping("/register")
    public RegisterCourse.Response registerCourse(
            RegisterCourse.Request request
    ) {
        CourseRegistration courseRegistration = courseFacade.registerCourse(
                new RegisterCourseCommand(
                        request.courseId(),
                        request.userId()
                )
        );
        return RegisterCourse.Response.builder()
                .courseId(courseRegistration.getCourseId())
                .studentId(courseRegistration.getStudentId())
                .createdAt(courseRegistration.getCreatedAt())
                .build();
    }

    @GetMapping("/register/user/:userId")
    public List<GetRegisteredCourses.Response> getRegisteredCourses(
            GetRegisteredCourses.Request request
    ) {
        List<CourseRegistrationInfo> courseInfos = courseFacade.viewRegisteredCourses(
                new ViewRegisteredCourseCommand(
                        request.userId()
                )
        );
        return courseInfos.stream().map(
                c -> GetRegisteredCourses.Response.builder()
                        .id(c.getId())
                        .name(c.getName())
                        .date(c.getDate())
                        .description(c.getDescription())
                        .build()
        ).toList();
    }
}
