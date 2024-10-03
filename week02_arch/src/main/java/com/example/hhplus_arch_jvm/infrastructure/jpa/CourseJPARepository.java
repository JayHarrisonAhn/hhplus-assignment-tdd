package com.example.hhplus_arch_jvm.infrastructure.jpa;

import com.example.hhplus_arch_jvm.infrastructure.jpa.entity.CourseJPA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface CourseJPARepository extends JpaRepository<CourseJPA, Long> {
    @Query("""
            SELECT c
            FROM
                CourseJPA c
                INNER JOIN
                CourseRegistrationCountJPA r
                ON c.id=r.course.id
            WHERE
                c.date=:date
            """)
    List<CourseJPA> findAllByDateEqualsAndRegistrationCount(@Param("date") LocalDate date);

}
