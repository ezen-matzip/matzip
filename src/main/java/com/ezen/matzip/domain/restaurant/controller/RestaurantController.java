package com.ezen.matzip.domain.restaurant.controller;

import com.ezen.matzip.domain.restaurant.dto.RestaurantDTO;
import com.ezen.matzip.domain.restaurant.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Slf4j
@Controller
@RequestMapping
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;


//    @GetMapping("/{restaurantCode}")
//    public String getRestaurantDetail(@PathVariable int restaurantCode, Model model) {
//        RestaurantDTO restaurant = restaurantService.getRestaurantDetail(restaurantCode);
//        model.addAttribute("restaurant", restaurant);
//        return "restaurant/restaurant";
//    }


//    @GetMapping("/result")
//    public String findByKeyword(@RequestParam String keyword, Model model)
//    {
//        List<RestaurantDTO> restaurants = restaurantService.findByKeywordOrderByScore(keyword);
//        model.addAttribute("restaurantList", restaurants);
//        return "test/result";
//    }

    @GetMapping("/store/search")
    public String findByMyLocation(@RequestParam String keyword, Model model)
    {
        List<RestaurantDTO> restaurants = restaurantService.findByKeywordOrderByScore(keyword);
        model.addAttribute("restaurantList", restaurants);
        return "domain/search/user_restlist";
    }

    @GetMapping("/store/storeinfo")
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
