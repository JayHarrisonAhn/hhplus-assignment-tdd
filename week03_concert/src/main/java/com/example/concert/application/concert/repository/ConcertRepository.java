package com.example.concert.application.concert.repository;

import com.example.concert.domain.Concert;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConcertRepository extends JpaRepository<Concert, Long> {
}
