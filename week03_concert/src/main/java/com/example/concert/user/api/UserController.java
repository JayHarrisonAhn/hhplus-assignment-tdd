package com.example.concert.user.api;

import com.example.concert.concert.api.ConcertControllerDTO;
import com.example.concert.concert.api.dto.ConcertTimeslotDTO;
import com.example.concert.user.UserFacade;
import com.example.concert.user.api.dto.UserDTO;
import com.example.concert.user.domain.User;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserFacade userFacade;

    @PostMapping("")
    @Operation(summary = "회원가입", description = "회원을 생성합니다.")
    UserControllerDTO.PostUser.Response postUser(
            @RequestBody UserControllerDTO.PostUser.Request request
    ) {
        User user = userFacade.createUser(request.getUsername());

        return UserControllerDTO.PostUser.Response.builder()
                .user(UserDTO.from(user))
                .build();
    }
}
