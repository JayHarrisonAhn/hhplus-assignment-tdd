package com.example.hhplus_arch_jvm.infrastructure.jpa;

import com.example.hhplus_arch_jvm.infrastructure.jpa.entity.CoachUserJPA;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoachJPARepository extends JpaRepository<CoachUserJPA, Long> {

}
