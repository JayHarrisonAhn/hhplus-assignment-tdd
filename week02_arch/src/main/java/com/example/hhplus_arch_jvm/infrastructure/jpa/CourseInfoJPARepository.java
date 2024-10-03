package com.example.hhplus_arch_jvm.infrastructure.jpa;

import com.example.hhplus_arch_jvm.application.domain.CourseRegistrationInfo;
import com.example.hhplus_arch_jvm.infrastructure.jpa.entity.CourseInfoJPA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface CourseInfoJPARepository extends JpaRepository<CourseInfoJPA, Long> {

    @Query("""
            SELECT c, r
            FROM
                CourseInfoJPA c
                INNER JOIN
                CourseRegistrationCountJPA r
                ON c.id=r.courseId
            WHERE
                c.date=:date
            """)
    List<CourseInfoJPA> findAllRegistrableByDate(@Param("date") LocalDate date);

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
    List<CourseRegistrationInfo> findAllRegisteredByStudentId(@Param("studentId") Long studentId);
}
