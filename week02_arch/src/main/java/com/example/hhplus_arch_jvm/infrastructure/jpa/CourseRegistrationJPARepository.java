package com.example.hhplus_arch_jvm.infrastructure.jpa;

import com.example.hhplus_arch_jvm.application.domain.CourseRegistrationInfo;
import com.example.hhplus_arch_jvm.infrastructure.jpa.entity.CourseRegistrationJPA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CourseRegistrationJPARepository extends JpaRepository<CourseRegistrationJPA, Long> {
    @Query("""
            SELECT new com.example.hhplus_arch_jvm.application.domain.CourseRegistrationInfo(
                ci.id, ci.name, ci.date, ci.description, cr.createdAt
            )
            FROM
                CourseRegistrationJPA cr
                INNER JOIN
                CourseInfoJPA ci
                ON cr.courseId=ci.id
            WHERE
                cr.studentId=:studentId
            """)
    List<CourseRegistrationInfo> findAllRegisteredCourseInfoByStudentId(@Param("studentId") Long studentId);
}
