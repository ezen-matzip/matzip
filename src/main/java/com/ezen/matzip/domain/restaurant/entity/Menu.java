package com.ezen.matzip.domain.restaurant.entity;

import com.ezen.matzip.domain.restaurant.dto.RestaurantDTO;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="menu")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Menu {

    @Id
    private int menuCode;
    private String menuName;
    private int menuPrice;

    @ManyToOne
    @JoinColumn(name = "restaurant_code")
    private Restaurant restaurantCode;

    public Menu(String menuName, Integer menuPrice, Restaurant restaurantCode) {
        this.restaurantCode =restaurantCode;
        this.menuName = menuName;
        this.menuPrice = menuPrice;
    }

    public void setRestaurantCode(Restaurant restaurantCode) {
        this.restaurantCode = restaurantCode;
    }


}
