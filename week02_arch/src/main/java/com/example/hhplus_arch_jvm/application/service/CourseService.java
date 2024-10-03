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

@Service
@RequiredArgsConstructor
public class CourseService {
    private final CourseInfoRepository courseInfoRepository;
    private final CourseRegistrationRepository courseRegistrationRepository;
    private final CourseRegistrationCountRepository courseRegistrationCountRepository;

    public List<CourseInfo> getAvailableCourses(LocalDate date) {
        return this.courseInfoRepository
                .findAllByDateEqualsAndRegistrationCount(date);
    }

    public CourseRegistrationCount decrementRegistrationCount(Long courseId) {
        CourseRegistrationCount courseRegistrationCount = this.courseRegistrationCountRepository
                .find(courseId);

        if (courseRegistrationCount.getCount() >= courseRegistrationCount.getMax()) {
            throw new IllegalStateException("잔여 자리 없음");
        }

        courseRegistrationCount.setCount(
                courseRegistrationCount.getCount() - 1
        );

        return this.courseRegistrationCountRepository.save(courseRegistrationCount);
    }

    public CourseRegistration addCourseRegistration(Long courseId, Long studentId) {
        CourseRegistration courseRegistration = CourseRegistration.builder()
                .courseId(courseId)
                .studentId(studentId)
                .createdAt(LocalDateTime.now())
                .build();

        return this.courseRegistrationRepository
                .save(courseRegistration);
    }

    public List<CourseRegistrationInfo> getRegisteredCourses(Long studentId) {
        return this.courseRegistrationRepository.findAllByUserId(studentId);
    }
}
