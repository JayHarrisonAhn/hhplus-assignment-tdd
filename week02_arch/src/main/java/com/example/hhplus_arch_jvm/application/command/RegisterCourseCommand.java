package com.example.hhplus_arch_jvm.application.command;

import java.time.LocalDate;

public record RegisterCourseCommand(
        long courseId,
        LocalDate date,
        long userId
) {
}
