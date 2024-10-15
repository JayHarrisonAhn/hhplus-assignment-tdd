package com.example.concert.infra.user;

import com.example.concert.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepositoryJPA extends JpaRepository<User, Long> {
}
