package com.example.hhplus_arch_jvm.infrastructure.jpa.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class CoachUserJPA {

    @Id @GeneratedValue
    private Long id;

    private String name;
}
