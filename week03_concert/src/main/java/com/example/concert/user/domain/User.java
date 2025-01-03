package com.example.concert.user.domain;

import com.example.concert.common.error.CommonErrorCode;
import com.example.concert.common.error.CommonException;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "USER_TABLE")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String username;

    public void validate() {
        if (this.username == null || this.username.isEmpty()) {
            throw new CommonException(CommonErrorCode.USER_PARAM_INSUFFICIENT);
        }
    }
}
