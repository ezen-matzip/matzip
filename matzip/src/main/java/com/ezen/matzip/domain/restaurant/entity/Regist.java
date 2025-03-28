//package com.ezen.matzip.domain.restaurant.entity;
//
//import jakarta.persistence.*;
//import lombok.AccessLevel;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//
//import java.sql.Time;
//import java.util.Date;
//import java.util.List;
//
//@Entity
//@Table(name = "restaurant")
//@Getter
//@Setter
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
//public class Regist {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "restaurant_code")
//    private int restaurantCode;
//    private String restaurantName;
//    private String restaurantLocation;
//    private String restaurantContactNumber;
//    private String restaurantDescription;
//    private String mainMenu;
//    private Time restaurantStartTime;
//    private Time restaurantEndTime;
//    private String restaurantService;
//    private Date restaurantRegistrationDate;
//    private int restaurantActiveStatus;
//    private String restaurantUniqueKeywords;
//    private int restaurantStatus;
//
//    public Regist(int restaurantCode, String restaurantName, String restaurantLocation, String restaurantContactNumber, String restaurantDescription, String mainMenu, Time restaurantStartTime, Time restaurantEndTime, String restaurantService, List<Menu> menu, List<Keyword> keyword ) {
//        this.restaurantCode = restaurantCode;
//        this.restaurantName = restaurantName;
//        this.restaurantLocation = restaurantLocation;
//        this.restaurantContactNumber = restaurantContactNumber;
//        this.restaurantDescription = restaurantDescription;
//        this.mainMenu = mainMenu;
//        this.restaurantStartTime = restaurantStartTime;
//        this.restaurantEndTime = restaurantEndTime;
//        this.restaurantService = restaurantService;
//        this.menu = menu;
//        this.keyword = keyword;
//    }
//
////    @ManyToOne
////    @JoinColumn(name = "business_code")
//    private int businessCode;
//    private int categoryCode;
//    @OneToMany
//    @JoinColumn(name = "restaurant_code")
//    private List<Menu> menu;
//
//    @OneToMany
//    @JoinColumn(name="restaurant_code")
//    private List<Keyword> keyword;
//
//}
