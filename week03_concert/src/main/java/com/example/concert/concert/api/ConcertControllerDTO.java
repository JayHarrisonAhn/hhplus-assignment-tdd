package com.example.concert.concert.api;

import com.example.concert.balance.api.dto.BalanceHistoryDTO;
import com.example.concert.concert.api.dto.ConcertSeatDTO;
import com.example.concert.concert.api.dto.ConcertTimeslotDTO;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

public class ConcertControllerDTO {

    public static class GetAvailableTimeslots {
        @Getter public static class Request {
            String token;
        }
        @Builder @Getter public static class Response {
            List<ConcertTimeslotDTO> timeSlots;
        }
    }

    public static class GetAvailableSeats {
        @Getter public static class Request {
            String token;
        }
        @Builder @Getter public static class Response {
            List<ConcertSeatDTO> seats;
        }
    }

    public static class OccupySeat {
        @Getter public static class Request {
            Long userId;
            String token;
        }
        @Builder @Getter public static class Response {
            ConcertSeatDTO seat;
        }
    }

    public static class PayReservation {
        @Getter public static class Request {
            Long userId;
            String token;
        }
        @Builder @Getter public static class Response {
            ConcertSeatDTO seat;
            BalanceHistoryDTO payHistory;
        }
    }
}
