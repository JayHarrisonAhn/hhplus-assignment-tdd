package com.example.hhplus_arch_jvm.integration;

import com.example.hhplus_arch_jvm.application.domain.CourseInfo;
import com.example.hhplus_arch_jvm.application.facade.CourseFacade;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

@SpringBootTest
public class ConcurrentTest {

    @Autowired private CourseFacade courseFacade;

    @Test
    void test40StudentsRegisterToOneCourse() {
        CourseInfo courseInfo = courseFacade.createCourse(
                CourseInfo.builder()
                        .date(LocalDate.of(2025, 1, 1))
                        .name("타일러 코치의 영어 잘하는 법")
                        .description("")
                        .build()
        );
    }
}
