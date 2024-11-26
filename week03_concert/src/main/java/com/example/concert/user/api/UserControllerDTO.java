package com.example.concert.user.api;

import com.example.concert.concert.api.dto.ConcertTimeslotDTO;
import com.example.concert.user.api.dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class UserControllerDTO {

    public static class PostUser {
        @Getter public static class Request {
            String username;
        }
        @Builder @Getter @AllArgsConstructor @NoArgsConstructor
        public static class Response {
            UserDTO user;
        }
    }
}
