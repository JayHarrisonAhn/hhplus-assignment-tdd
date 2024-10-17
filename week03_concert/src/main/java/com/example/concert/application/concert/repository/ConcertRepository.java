package com.example.concert.application.concert.repository;

import com.example.concert.domain.Concert;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConcertRepository extends JpaRepository<Concert, Long> {

    Optional<Concert> findById(Long id);
}
