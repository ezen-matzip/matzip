package com.ezen.matzip.domain.restaurant.entity;

import com.ezen.matzip.domain.restaurant.dto.MenuDTO;
import com.ezen.matzip.domain.review.entity.Review;
import com.ezen.matzip.domain.user.entity.Business;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Time;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table(name = "restaurant")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "restaurant_code")
    private int restaurantCode;
    private String restaurantName;
    private String restaurantLocation;
    private String restaurantContactNumber;
    private String restaurantDescription;
    private String mainMenu;
    private Time restaurantStartTime;
    private Time restaurantEndTime;
    private String restaurantService;
    private Date restaurantRegistrationDate;
    private int restaurantActiveStatus;
    private String restaurantUniqueKeywords;
    private int businessCode;
    private int restaurantStatus;

    @ManyToOne
    @JoinColumn(name = "category_code")
    private Category category;

    public Restaurant(int restaurantCode, String restaurantName, String restaurantLocation, String restaurantContactNumber,
                      String restaurantDescription, String mainMenu, Time restaurantStartTime, Time restaurantEndTime,
                      String restaurantService, Category restaurantCategory, int businessCode) {
        this.restaurantCode = restaurantCode;
        this.restaurantName = restaurantName;
        this.restaurantLocation = restaurantLocation;
        this.restaurantContactNumber = restaurantContactNumber;
        this.restaurantDescription = restaurantDescription;
        this.mainMenu = mainMenu;
        this.restaurantStartTime = restaurantStartTime;
        this.restaurantEndTime = restaurantEndTime;
        this.restaurantService = restaurantService;
        this.category = restaurantCategory;
        this.businessCode = businessCode;
    }

    public void Modify(int restaurantCode, String restaurantName, String restaurantLocation, String restaurantContactNumber,
                       String restaurantDescription, String mainMenu, Time restaurantStartTime, Time restaurantEndTime,
                       String restaurantService, Category restaurantCategory, int businessCode) {
        this.restaurantCode = restaurantCode;
        this.restaurantName = restaurantName;
        this.restaurantLocation = restaurantLocation;
        this.restaurantContactNumber = restaurantContactNumber;
        this.restaurantDescription = restaurantDescription;
        this.mainMenu = mainMenu;
        this.restaurantStartTime = restaurantStartTime;
        this.restaurantEndTime = restaurantEndTime;
        this.restaurantService = restaurantService;
        this.category = restaurantCategory;
        this.businessCode = businessCode;
    }

    @OneToMany(mappedBy = "restaurantCode", cascade = CascadeType.ALL)
    private List<Menu> menus;

    @OneToMany
    private List<Review> reviews;

    @OneToMany(mappedBy = "restaurantCode", cascade = CascadeType.ALL)
    private List<RestaurantKeyword> restaurantKeywords;

    @OneToMany(mappedBy = "restaurantCode", cascade = CascadeType.ALL)
    private List<RestaurantImage> restaurantImages;



    @Builder
    public Restaurant(int restaurantCode, String restaurantName) {
        this.restaurantCode = restaurantCode;
        this.restaurantName = restaurantName;
    }
}
