package com.example.hhplus_arch_jvm.infrastructure.jpa.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter @Setter
public class CourseJPA {

    @Id @GeneratedValue
    Long id;

    String name;

    LocalDate date;

}
