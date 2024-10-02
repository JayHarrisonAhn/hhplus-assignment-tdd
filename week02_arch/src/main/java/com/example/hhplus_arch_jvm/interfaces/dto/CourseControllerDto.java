package com.example.hhplus_arch_jvm.interfaces.dto;

import java.time.LocalDate;

public class CourseControllerDto {
    public static class GetAllAvailableCourses {
        public record Request(
                LocalDate date
        ) {}
        public record Response() {}
    }
    public static class RegisterCourse {
        public record Request(
                long userId,
                long courseId,
                LocalDate date
        ) {}
        public record Response() {}
    }
    public static class GetRegisteredCourses {
        public record Request(
                long userId
        ) {}
        public record Response() {}
    }
}
