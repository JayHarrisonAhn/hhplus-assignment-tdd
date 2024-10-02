package com.example.hhplus_arch_jvm.infrastructure.repository;

import com.example.hhplus_arch_jvm.application.repository.CourseRepository;
import com.example.hhplus_arch_jvm.infrastructure.jpa.CoachJPARepository;
import com.example.hhplus_arch_jvm.infrastructure.jpa.CourseRegistrationJPARepository;
import com.example.hhplus_arch_jvm.infrastructure.jpa.StudentJPARepository;
import com.example.hhplus_arch_jvm.infrastructure.jpa.UserJPARepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Primary
@Component
@RequiredArgsConstructor
public class CourseRepositoryImpl implements CourseRepository {

    private final CoachJPARepository coachJPARepository;
    private final CourseRegistrationJPARepository courseRegistrationJPARepository;
    private final StudentJPARepository studentJPARepository;
    private final UserJPARepository userJPARepository;
}
