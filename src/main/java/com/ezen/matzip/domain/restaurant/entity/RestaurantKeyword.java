package com.ezen.matzip.domain.restaurant.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "restaurant_star_keyword")
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RestaurantKeyword {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int restaurantKeywordCode;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_code")
    private Restaurant restaurantCode;
    private String restaurantKeyword;

    public RestaurantKeyword(String restaurantKeyword, Restaurant restaurant) {
        this.restaurantKeyword = restaurantKeyword;
        this.restaurantCode = restaurant;
    }

    public void ModifyKeyword(String restaurantKeyword) {
        this.restaurantKeyword = restaurantKeyword;
    }

//    public void setRestaurantCode(Restaurant restaurantCode) {
//        this.restaurantCode = restaurantCode;
//    }
}


