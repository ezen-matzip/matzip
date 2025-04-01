package com.ezen.matzip.domain.restaurant.dto;

import com.ezen.matzip.domain.restaurant.entity.Category;
import com.ezen.matzip.domain.restaurant.entity.RestaurantKeyword;
import com.ezen.matzip.domain.restaurant.entity.Menu;
import com.ezen.matzip.domain.restaurant.entity.Restaurant;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Time;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
public class RegistDTO {

    private int restaurantCode;
    private String restaurantName;
    private String restaurantLocation;
    private String restaurantContactNumber;
    private String restaurantDescription;
    private String mainMenu;
    private String restaurantStartTime;
    private String restaurantEndTime;
    private String restaurantService;
    private List<String> menuName;
    private List<Integer> menuPrice;
    private List<String> restaurantKeyword;
    private String restaurantCategory;
}
