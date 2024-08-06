package com.jeong.simplex.common;

import jakarta.persistence.*;
import lombok.Data;


@Data
@Entity(name = "TBL_USER")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
}