package com.example.concert.interfaces.dto;

import com.example.concert.interfaces.dto.entity.ConcertSeatDTO;
import com.example.concert.interfaces.dto.entity.ConcertTimeslotDTO;
import com.example.concert.interfaces.dto.entity.PayHistoryDTO;
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
            PayHistoryDTO payHistory;
        }
    }
}
