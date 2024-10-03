package com.example.hhplus_arch_jvm.infrastructure.repository;

import com.example.hhplus_arch_jvm.application.domain.CourseInfo;
import com.example.hhplus_arch_jvm.application.repository.CourseInfoRepository;
import com.example.hhplus_arch_jvm.infrastructure.jpa.CourseJPARepository;
import com.example.hhplus_arch_jvm.infrastructure.jpa.entity.CourseInfoJPA;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Primary
@Component
@RequiredArgsConstructor
public class CourseInfoRepositoryImpl implements CourseInfoRepository {

    private CourseJPARepository courseJPARepository;

    @Override
    public CourseInfo createCourse(CourseInfo courseInfo) {
        return null;
    }

    @Override
    public List<CourseInfo> findAllByDateEqualsAndRegistrationCount(LocalDate date) {
        return courseJPARepository.findAllByDateEqualsAndRegistrationCount(date).stream()
                .map(CourseInfoJPA::toDomain)
                .toList();
    }
}
