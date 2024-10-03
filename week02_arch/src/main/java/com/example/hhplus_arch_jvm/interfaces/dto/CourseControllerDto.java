package com.example.hhplus_arch_jvm.interfaces.dto;

import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class CourseControllerDto {
    public static class GetAllAvailableCourses {
        public record Request(
                LocalDate date
        ) {}
        @Builder
        public record Response(
                Long id,
                String name,
                LocalDate date,
                String description
        ) {}
    }
    public static class RegisterCourse {
        public record Request(
                long userId,
                long courseId
        ) {}
        @Builder
        public record Response(
                Long courseId,
                Long studentId,
                LocalDateTime createdAt
        ) {}
    }
    public static class GetRegisteredCourses {
        public record Request(
                long userId
        ) {}
        @Builder
        public record Response(
                Long id,
                String name,
                LocalDate date,
                String description,
                LocalDateTime registeredAt
        ) {}
    }
}
