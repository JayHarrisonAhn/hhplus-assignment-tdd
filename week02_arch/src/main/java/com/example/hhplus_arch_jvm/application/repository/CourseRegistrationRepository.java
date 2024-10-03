package com.example.hhplus_arch_jvm.application.repository;

import com.example.hhplus_arch_jvm.application.domain.CourseRegistration;
import com.example.hhplus_arch_jvm.application.domain.CourseRegistrationInfo;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface CourseRegistrationRepository {

    CourseRegistration save(CourseRegistration courseRegistration);
    List<CourseRegistrationInfo> findAllByUserId(Long userId);
}
