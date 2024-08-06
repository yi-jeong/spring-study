package com.jeong.simplex.common;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@Entity(name = "TBL_POST")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String postTitle;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}