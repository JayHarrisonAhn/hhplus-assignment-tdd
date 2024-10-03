package com.example.hhplus_arch_jvm.application.service;

import com.example.hhplus_arch_jvm.application.domain.CourseInfo;
import com.example.hhplus_arch_jvm.application.repository.CourseInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseService {
    private final CourseInfoRepository courseInfoRepository;

    public List<CourseInfo> getAvailableCourses(LocalDate date) {
        return this.courseInfoRepository
                .findAllByDateEqualsAndRegistrationCount(date);
    }
}
