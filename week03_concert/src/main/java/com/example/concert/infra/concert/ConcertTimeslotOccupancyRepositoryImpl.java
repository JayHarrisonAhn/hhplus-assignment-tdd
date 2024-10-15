package com.example.concert.infra.concert;

import com.example.concert.application.concert.repository.ConcertTimeslotOccupancyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Primary
@Component
@RequiredArgsConstructor
public class ConcertTimeslotOccupancyRepositoryImpl implements ConcertTimeslotOccupancyRepository {
}
