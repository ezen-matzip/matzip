package com.ezen.matzip.domain.restaurant.dto;

import com.ezen.matzip.domain.restaurant.entity.Restaurant;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MenuDTO {
    private int menuCode;
    private String menuName;
    private int menuPrice;
    private int restaurantCode;

    public MenuDTO(int menuCode, String menuName, int menuPrice, Restaurant restaurantCode) {
        this.menuCode = menuCode;
        this.menuName = menuName;
        this.menuPrice = menuPrice;
        this.restaurantCode = restaurantCode.getRestaurantCode();
    }
}
