package com.example.hhplus_arch_jvm.application.service;

import com.example.hhplus_arch_jvm.application.domain.CourseInfo;
import com.example.hhplus_arch_jvm.application.domain.CourseRegistration;
import com.example.hhplus_arch_jvm.application.domain.CourseRegistrationCount;
import com.example.hhplus_arch_jvm.application.domain.CourseRegistrationInfo;
import com.example.hhplus_arch_jvm.application.repository.CourseInfoRepository;
import com.example.hhplus_arch_jvm.application.repository.CourseRegistrationCountRepository;
import com.example.hhplus_arch_jvm.application.repository.CourseRegistrationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CourseService {
    private final CourseInfoRepository courseInfoRepository;
    private final CourseRegistrationRepository courseRegistrationRepository;
    private final CourseRegistrationCountRepository courseRegistrationCountRepository;

    public CourseInfo createCourseInfo(CourseInfo courseInfo) {
        return this.courseInfoRepository.save(courseInfo);
    }

    public CourseRegistrationCount createCourseRegistrationCount(Long courseId) {
        return this.courseRegistrationCountRepository.save(
                CourseRegistrationCount.builder()
                        .courseId(courseId)
                        .count(0)
                        .max(30)
                        .build()
        );
    }

    public List<CourseInfo> getAvailableCourses(LocalDate date) {
        return this.courseInfoRepository
                .findAllRegistrableByDate(date);
    }

    public CourseRegistrationCount decrementRegistrationCount(Long courseId) {
        CourseRegistrationCount courseRegistrationCount = this.courseRegistrationCountRepository
                .find(courseId);

        if (courseRegistrationCount.getCount() >= courseRegistrationCount.getMax()) {
            throw new IllegalStateException("잔여 자리 없음");
        }

        courseRegistrationCount.setCount(
                courseRegistrationCount.getCount() + 1
        );

        return this.courseRegistrationCountRepository.save(courseRegistrationCount);
    }

    public CourseRegistration addCourseRegistration(Long courseId, Long studentId) {
        this.courseRegistrationRepository.find(courseId, studentId)
                .ifPresent( __ -> {
                    throw new IllegalStateException("이미 신청내역이 존재합니다");
                });

        CourseRegistration courseRegistration = CourseRegistration.builder()
                .courseId(courseId)
                .studentId(studentId)
                .createdAt(LocalDateTime.now())
                .build();

        CourseInfo courseInfo = this.courseInfoRepository.find(courseId)
                .orElseThrow(() -> new NoSuchElementException(""));

        if (courseInfo.getDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("이미 진행된 강의입니다.");
        }

        return this.courseRegistrationRepository
                .save(courseRegistration);
    }

    public List<CourseRegistrationInfo> getRegisteredCourses(Long studentId) {
        return this.courseInfoRepository.findAllRegisteredByStudentId(studentId);
    }
}
