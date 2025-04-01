package com.ezen.matzip.domain.review.dto;

import com.ezen.matzip.domain.restaurant.entity.Restaurant;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@Setter
@Getter
@ToString
public class ReviewDTO {

    private int reviewCode;
    private String reviewContent;
    private Date reviewDate;
    private int reviewReportCount;
    private int hiddenFlag;
    private String reviewReply;
    private int rating;
    private int userCode;
    private int businessCode;
    private int restaurantCode;
    private int reservationCode;

    private String restaurantName;

    private List<ReviewImageDTO> reviewImages;

    public void setRestaurantCode(Restaurant restaurant) {
        this.restaurantCode = restaurant.getRestaurantCode();
    }

    public void setRestaurantName(Restaurant restaurant) {
        this.restaurantName = restaurant.getRestaurantName();
    }
}
