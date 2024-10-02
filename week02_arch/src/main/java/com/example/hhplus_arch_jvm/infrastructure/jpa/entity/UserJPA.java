package com.example.hhplus_arch_jvm.infrastructure.jpa.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Getter @Setter
public class UserJPA {

    @Id @GeneratedValue
    private Long id;
}
