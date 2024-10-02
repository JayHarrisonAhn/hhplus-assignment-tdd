package com.example.hhplus_arch_jvm.infrastructure.jpa;

import com.example.hhplus_arch_jvm.infrastructure.jpa.entity.StudentUserJPA;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentJPARepository extends JpaRepository<StudentUserJPA, Long> {
}
