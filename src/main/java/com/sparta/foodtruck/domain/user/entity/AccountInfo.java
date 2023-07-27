package com.sparta.foodtruck.domain.user.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AccountInfo {
    @Id
    @Column(name = "user_id")
    private Long id;

    private String username;

    private String introduce;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;

    public AccountInfo(Long id, String username) {
        this.id = id;
        this.username = username;
        this.introduce = null;
        this.role = UserRoleEnum.USER;
    }
}
