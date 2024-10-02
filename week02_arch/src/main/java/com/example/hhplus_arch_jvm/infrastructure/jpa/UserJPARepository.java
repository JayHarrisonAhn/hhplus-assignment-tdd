package com.example.hhplus_arch_jvm.infrastructure.jpa;

import com.example.hhplus_arch_jvm.infrastructure.jpa.entity.UserJPA;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJPARepository extends JpaRepository<UserJPA, Long> {
}
