package com.ezen.matzip.domain.restaurant.dto;

import com.ezen.matzip.domain.restaurant.entity.Menu;
import com.ezen.matzip.domain.restaurant.entity.Restaurant;
import com.ezen.matzip.domain.restaurant.entity.Keyword;
import com.ezen.matzip.domain.review.entity.Review;

import lombok.*;

import java.sql.Time;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
public class RestaurantDTO {

    private String restaurantName;
    private List<MenuDTO> restaurantMenus;
    private List<RestaurantStarKeywordDTO> restaurantKeywords;
    private List<Review> reviews;
    private int restaurantCode;
    private String restaurantLocation;
    private String restaurantContactNumber;
    private String restaurantDescription;
    private Date restaurantRegistrationDate;
    private int restaurantActiveStatus;
    private String restaurantUniqueKeywords;
    private String mainMenu;
    private int businessCode;
    private CategoryDTO categoryCode;
    private String restaurantStartTime;
    private String restaurantEndTime;
    private int restaurantStatus;
    private String restaurantService;

    public RestaurantDTO (Restaurant restaurant, List<Menu> menus, List<Keyword> keywords)
    {
        this.restaurantName = restaurant.getRestaurantName();
        this.restaurantMenus = menus.stream().map
                (menu -> new MenuDTO(menu.getMenuCode(), menu.getMenuName(), menu.getMenuPrice(), restaurant))
                .collect(Collectors.toList());
        this.restaurantKeywords = keywords.stream().map
                (keyword -> new RestaurantStarKeywordDTO(keyword.getRestaurantKeywordCode(), keyword.getRestaurantCode().getRestaurantCode(), keyword.getRestaurantKeyword()))
                .collect(Collectors.toList());
        this.restaurantCode = restaurant.getRestaurantCode();
        this.restaurantLocation = restaurant.getRestaurantLocation();
        this.restaurantContactNumber = restaurant.getRestaurantContactNumber();
        this.restaurantDescription = restaurant.getRestaurantDescription();
        this.restaurantRegistrationDate = restaurant.getRestaurantRegistrationDate();
        this.restaurantActiveStatus = restaurant.getRestaurantActiveStatus();
        this.restaurantUniqueKeywords = restaurant.getRestaurantUniqueKeywords();
        this.mainMenu = restaurant.getMainMenu();
        this.businessCode = restaurant.getBusinessCode();
        this.categoryCode = new CategoryDTO(restaurant.getCategory().getCategoryCode(), restaurant.getCategory().getCategory());
        this.restaurantStartTime = getRestaurantStartTime();
        this.restaurantEndTime = getRestaurantEndTime();
        this.restaurantStatus = restaurant.getRestaurantStatus();
        this.restaurantService = restaurant.getRestaurantService();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        RestaurantDTO other = (RestaurantDTO) obj;
        return this.getRestaurantCode() == (other.getRestaurantCode());
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(getRestaurantCode());
    }
}
