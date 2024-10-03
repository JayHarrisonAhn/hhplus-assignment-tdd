package com.example.hhplus_arch_jvm.application.service;

import com.example.hhplus_arch_jvm.application.domain.CourseInfo;
import com.example.hhplus_arch_jvm.application.domain.CourseRegistrationCount;
import com.example.hhplus_arch_jvm.application.repository.CourseInfoRepository;
import com.example.hhplus_arch_jvm.application.repository.CourseRegistrationCountRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.*;

class CourseServiceTest {
    @InjectMocks
    private CourseService courseService;

    ////// Mock Setups //////
    @Mock
    private CourseInfoRepository courseInfoRepository;

    @Mock
    private CourseRegistrationCountRepository courseRegistrationCountRepository;

    private AutoCloseable closeable;


    ////// Example Data //////
    private CourseInfo exampleCourseInfo;
    private CourseRegistrationCount exampleCourseRegistrationCount;

    ////// Tests //////
    @BeforeEach
    void setUp() {
        this.closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    void setUpExampleCourse() {
        // Example Data 삽입
        this.exampleCourseInfo = CourseInfo.builder()
                .id(1L)
                .date(LocalDate.of(2024, 10, 1))
                .name("테스트 특강")
                .description("어쩌구저쩌구")
                .build();

        this.exampleCourseRegistrationCount = CourseRegistrationCount.builder()
                .courseId(this.exampleCourseInfo.getId())
                .count(20)
                .max(30)
                .build();

        // courseRegistrationCount save 시 parameter를 그대로 반환
        doAnswer( invocationOnMock -> invocationOnMock.getArgument(0))
                .when(this.courseRegistrationCountRepository)
                .save(
                        any(CourseRegistrationCount.class)
                );

        // courseRegistrationCount save 시 example date 그대로 반환
        when(
                this.courseRegistrationCountRepository.find(this.exampleCourseInfo.getId())
        ).thenReturn(this.exampleCourseRegistrationCount);
    }

    @Test
    @DisplayName("성공 케이스")
    void decrementRegistrationCount_success() {
        this.setUpExampleCourse();

        Integer originalCount = this.exampleCourseRegistrationCount.getCount();

        CourseRegistrationCount courseRegistrationCount = this.courseService
                .decrementRegistrationCount(
                        this.exampleCourseInfo.getId()
                );

        assertEquals(
                originalCount - 1,
                courseRegistrationCount.getCount()
        );
    }
}