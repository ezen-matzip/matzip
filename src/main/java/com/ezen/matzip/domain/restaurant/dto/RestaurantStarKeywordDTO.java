package com.ezen.matzip.domain.restaurant.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RestaurantStarKeywordDTO {
    private int restaurantKeywordCode;
    private int restaurantCode;
    private String restaurantKeyword;

    public RestaurantStarKeywordDTO(int restaurantKeywordCode, int restaurantCode, String restaurantKeyword) {
        this.restaurantKeywordCode = restaurantKeywordCode;
        this.restaurantCode = restaurantCode;
        this.restaurantKeyword = restaurantKeyword;
    }
}
