package com.example.hhplus_arch_jvm.application.command;

import java.time.LocalDate;

public record ViewRegisteredCourseCommand(
        long userId,
        LocalDate date
) {
}
