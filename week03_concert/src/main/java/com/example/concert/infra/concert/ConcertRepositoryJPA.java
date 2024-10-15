package com.example.concert.infra.concert;

import com.example.concert.domain.Concert;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConcertRepositoryJPA extends JpaRepository<Concert, Long> {
}
