package com.ezen.matzip.domain.restaurant.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="Business")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Business {

    @Id
    private int businessCode;
    private String userId;
    private String password;
    private String phoneNumber;
    private String passwordQuestion;
    private String passwordAnswer;
    private String businessNumber;
    private String restaurantName;
    private String email;
    private int role;
}
