package com.ezen.matzip.domain.review.entity;

import com.ezen.matzip.domain.restaurant.entity.Restaurant;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "review")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int reviewCode;
    private String reviewContent;
    private Date reviewDate;
    private int reviewReportCount;
    private int hiddenFlag;
    private String reviewReply;
    private int rating;
    private int userCode;
    private int businessCode;
    @ManyToOne
    @JoinColumn(name = "restaurant_code")
    private Restaurant restaurantCode;
    private int reservationCode;

//    setter 필요한 필드만 setter 만들어서 사용
}
