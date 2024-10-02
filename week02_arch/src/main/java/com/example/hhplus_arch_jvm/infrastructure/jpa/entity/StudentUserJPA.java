package com.example.hhplus_arch_jvm.infrastructure.jpa.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class StudentUserJPA extends UserJPA {

    private Integer generation;
}
