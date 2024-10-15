package com.example.concert.infra.concert;

import com.example.concert.application.concert.repository.ConcertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Primary
@Component
@RequiredArgsConstructor
public class ConcertRepositoryImpl implements ConcertRepository {
}
