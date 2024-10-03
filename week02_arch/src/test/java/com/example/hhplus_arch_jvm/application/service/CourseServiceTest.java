package com.example.hhplus_arch_jvm.application.service;

import com.example.hhplus_arch_jvm.application.domain.CourseInfo;
import com.example.hhplus_arch_jvm.application.domain.CourseRegistration;
import com.example.hhplus_arch_jvm.application.domain.CourseRegistrationCount;
import com.example.hhplus_arch_jvm.application.repository.CourseRegistrationCountRepository;
import com.example.hhplus_arch_jvm.application.repository.CourseRegistrationRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

class CourseServiceTest {
    @InjectMocks
    private CourseService courseService;

    ////// Mock Setups //////
    @Mock
    private CourseRegistrationRepository courseRegistrationRepository;

    @Mock
    private CourseRegistrationCountRepository courseRegistrationCountRepository;

    private AutoCloseable closeable;


    ////// Example Data //////
    private CourseInfo exampleCourseInfo;
    private CourseRegistrationCount exampleCourseRegistrationCount;

    ////// Test Setups //////
    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    ////// Tests //////
    void setUpExampleCourse() {
        // Example Data 삽입
        this.exampleCourseInfo = CourseInfo.builder()
                .id(1L)
                .date(LocalDate.of(2024, 10, 1))
                .name("테스트 특강")
                .coachDescription("어쩌구저쩌구")
                .build();

        this.exampleCourseRegistrationCount = CourseRegistrationCount.builder()
                .courseId(this.exampleCourseInfo.getId())
                .count(20)
                .max(30)
                .build();

        // courseRegistrationCount save 시 입력을 그대로 출력
        doAnswer( invocationOnMock -> invocationOnMock.getArgument(0))
                .when(this.courseRegistrationCountRepository)
                .save(
                        any(CourseRegistrationCount.class)
                );

        // courseRegistrationCount find 시 example data 반환
        when(
                this.courseRegistrationCountRepository.find(this.exampleCourseInfo.getId())
        ).thenReturn(this.exampleCourseRegistrationCount);
    }

    @Test
    @DisplayName("decrementRegistrationCount : 성공")
    void decrementRegistrationCount_success() {
        // Given
        this.setUpExampleCourse();

        Integer originalCount = this.exampleCourseRegistrationCount.getCount();

        // When
        CourseRegistrationCount courseRegistrationCount = this.courseService
                .decrementRegistrationCount(
                        this.exampleCourseInfo.getId()
                );

        // Then
        assertEquals(
                originalCount - 1,
                courseRegistrationCount.getCount()
        );
    }

    @Test
    @DisplayName("decrementRegistrationCount : 실패 - 남은 자리가 없는 경우")
    void decrementRegistrationCount_noAvailableSeat() {
        // Given
        this.setUpExampleCourse();
        this.exampleCourseRegistrationCount.setCount(
                this.exampleCourseRegistrationCount.getMax()
        );

        // When, Then
        assertThrows(
                IllegalStateException.class,
                () -> this.courseService
                        .decrementRegistrationCount(
                                this.exampleCourseInfo.getId()
                        )
        );
    }

    // 아래서부터는 읽기 쉽게 작성(위에 코드는 시간 없어서 수정 못함 ㅠㅠ)
    @Test
    @DisplayName("addCourseRegistration : 성공")
    void addCourseRegistration_success() {
        // Given
        Long courseId = 1L;
        Long studentId = 2L;
        doAnswer( invocationOnMock -> invocationOnMock.getArgument(0))
                .when(this.courseRegistrationRepository)
                .save(
                        any(CourseRegistration.class)
                );

        // When
        CourseRegistration registration = courseService.addCourseRegistration(courseId, studentId);

        // Then
        assertEquals(courseId, registration.getCourseId());
        assertEquals(studentId, registration.getStudentId());
    }

    @Test
    @DisplayName("addCourseRegistration : 실패")
    void addCourseRegistration_alreadyRegistered() {
        // Given
        Long courseId = 1L;
        Long studentId = 2L;

        CourseRegistration existingRegistration = mock(CourseRegistration.class);
        when(existingRegistration.getCourseId()).thenReturn(courseId);
        when(existingRegistration.getStudentId()).thenReturn(studentId);

        when(
                courseRegistrationRepository.find(existingRegistration.getCourseId(), existingRegistration.getStudentId())
        ).thenReturn(Optional.of(existingRegistration));

        // When, Then
        assertThrows(
                IllegalStateException.class,
                () -> this.courseService
                        .addCourseRegistration(courseId, studentId)
        );
    }
}