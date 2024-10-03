package com.example.hhplus_arch_jvm.application.service;

import com.example.hhplus_arch_jvm.application.domain.CourseInfo;
import com.example.hhplus_arch_jvm.application.domain.CourseRegistrationCount;
import com.example.hhplus_arch_jvm.application.repository.CourseInfoRepository;
import com.example.hhplus_arch_jvm.application.repository.CourseRegistrationCountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseService {
    private final CourseInfoRepository courseInfoRepository;
    private final CourseRegistrationCountRepository courseRegistrationCountRepository;

    public List<CourseInfo> getAvailableCourses(LocalDate date) {
        return this.courseInfoRepository
                .findAllByDateEqualsAndRegistrationCount(date);
    }

    public CourseRegistrationCount decrementRegistrationCount(Long courseId) {
        CourseRegistrationCount courseRegistrationCount = this.courseRegistrationCountRepository
                .find(courseId);

        courseRegistrationCount.setCount(
                courseRegistrationCount.getCount() - 1
        );

        return this.courseRegistrationCountRepository.save(courseRegistrationCount);
    }
}
