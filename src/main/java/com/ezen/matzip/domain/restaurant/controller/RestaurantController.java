package com.ezen.matzip.domain.restaurant.controller;

import com.ezen.matzip.domain.restaurant.dto.RegistDTO;
import com.ezen.matzip.domain.restaurant.dto.RestaurantDTO;
import com.ezen.matzip.domain.restaurant.entity.Restaurant;
import com.ezen.matzip.domain.restaurant.service.RestaurantService;
import com.ezen.matzip.domain.review.dto.ReviewDTO;
import com.ezen.matzip.domain.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/restaurant")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;
//    private final ReviewService reviewService;


    @GetMapping("/{restaurantCode}")
    public String getRestaurantDetail(@PathVariable int restaurantCode, Model model) {
        RestaurantDTO restaurant = restaurantService.getRestaurantDetail(restaurantCode);
        model.addAttribute("restaurant", restaurant);

        List<ReviewDTO> resultReview = restaurantService.getReviewsByRestaurant(restaurantCode);
        model.addAttribute("reviews", resultReview);
        System.out.println("test: " + resultReview);


        return "restaurant/restaurant";
    }

    @GetMapping("/regist")
    public String registPage() {
        return "restaurant/restaurant-regist";
    }

    @PostMapping("/regist")
    public String regist(@ModelAttribute RegistDTO registDTO) {

        System.out.println("=== DTO 로그 ===");
        System.out.println(registDTO.toString());
     restaurantService.registRestaurant(registDTO);

        return "redirect:/restaurant/" + registDTO.getRestaurantCode();
    }

    @GetMapping("/modify")
    public String modifyPage() {
        return "restaurant/restaurant-modify";
    }

    @PostMapping("/modify")
    public String modify(@ModelAttribute RegistDTO registDTO) {
        System.out.println("modify: " + registDTO.toString());
        restaurantService.modifyRestaurant(registDTO);

        return "redirect:/restaurant/" + registDTO.getRestaurantCode();
    }

    @GetMapping("/search")
    public String findByMyLocation(@RequestParam String keyword, Model model)
    {
        List<RestaurantDTO> restaurants = restaurantService.findByKeywordOrderByScore(keyword);
        model.addAttribute("restaurantList", restaurants);
        model.addAttribute("myLoc", keyword);
        System.out.println("test: " + restaurants);
        return "domain/search/user_restlist";
    }

    @GetMapping("/storeinfo")
    public String markingLocation(@RequestParam Integer restaurantCode, Model model)
    {
        String location = restaurantService.findLocationByRestaurantCode(restaurantCode);
        model.addAttribute("restaurantLocation", location);
        return "/domain/restaurant/store_restinfo";
    }



}

//restaurant/ → 식당 관리 (등록, 수정, 조회)

//Controller → 클라이언트 요청을 처리
//Service → 비즈니스 로직을 처리
//Repository → 데이터베이스 CRUD 처리
//DTO → 클라이언트와 데이터를 주고받을 때 사용하는 객체
//Entity → 실제 데이터베이스 테이블과 매칭되는 객체
