package com.example.hhplus_arch_jvm.infrastructure.repository;

import com.example.hhplus_arch_jvm.application.domain.CourseInfo;
import com.example.hhplus_arch_jvm.application.domain.CourseRegistrationInfo;
import com.example.hhplus_arch_jvm.application.repository.CourseInfoRepository;
import com.example.hhplus_arch_jvm.infrastructure.jpa.CourseInfoJPARepository;
import com.example.hhplus_arch_jvm.infrastructure.jpa.entity.CourseInfoJPA;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Primary
@Component
@AllArgsConstructor
public class CourseInfoRepositoryImpl implements CourseInfoRepository {

    private CourseInfoJPARepository courseInfoJPARepository;

    @Override
    public List<CourseRegistrationInfo> findAllRegisteredByStudentId(Long studentId) {
        return courseInfoJPARepository.findAllRegisteredByStudentId(studentId);
    }

    @Override
    public CourseInfo save(CourseInfo courseInfo) {
        return courseInfoJPARepository.save(
                CourseInfoJPA.fromDomain(courseInfo)
        ).toDomain();
    }

    @Override
    public List<CourseInfo> findAllRegistrableByDate(LocalDate date) {
        return courseInfoJPARepository.findAllRegistrableByDate(date).stream()
                .map(CourseInfoJPA::toDomain)
                .toList();
    }
}
