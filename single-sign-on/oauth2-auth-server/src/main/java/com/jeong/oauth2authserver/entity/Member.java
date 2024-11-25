package com.jeong.oauth2authserver.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name="tbl_member")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true)
    private String username;
    private String password;


    public static Member create(String username, String password) {
        return Member.builder()
                .username(username)
                .password(password)
                .build();
    }

}