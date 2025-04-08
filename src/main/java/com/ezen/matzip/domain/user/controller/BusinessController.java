package com.ezen.matzip.domain.user.controller;

import com.ezen.matzip.domain.restaurant.dto.RestaurantDTO;
import com.ezen.matzip.domain.review.entity.Review;
import com.ezen.matzip.domain.review.service.ReviewAnswerService;
import com.ezen.matzip.domain.user.entity.Business;
import com.ezen.matzip.domain.user.entity.User;
import com.ezen.matzip.domain.user.repository.BusinessRepository;
import com.ezen.matzip.domain.user.service.BusinessService;
import com.ezen.matzip.domain.user.service.CustomUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;

@Controller
public class BusinessController {
    private final ReviewAnswerService reviewAnswerService;
    private final BusinessService businessService;

    public BusinessController(ReviewAnswerService reviewAnswerService, BusinessRepository businessRepository, BusinessService businessService) {
        this.reviewAnswerService = reviewAnswerService;
        this.businessService = businessService;
    }

    @GetMapping("/business/main")
    public String businessMain(@AuthenticationPrincipal CustomUserDetails userDetails, Model model, Principal principal) {
        if (userDetails != null) {

            Business business = userDetails.getBusiness();  // 👈 로그인한 유저 객체

            List<Review> recentReviews = reviewAnswerService.getRecentReview(business.getBusinessCode());
            model.addAttribute("businessCode", business.getBusinessCode());  // 👈 main.html에서 사용 가능
            model.addAttribute("recentReviews", recentReviews);

            // RestaurantDTO restaurantDTO = businessService.findRestaurantByUserId(principal.getName());
            // if (restaurantDTO != null) {
            //     model.addAttribute("myRestaurantCode", restaurantDTO.getRestaurantCode());
            // } else {
            //     // 레스토랑 정보가 없는 경우, 별도의 처리가 필요하다면 여기서 수행
            //     // 예를 들어, model.addAttribute("restaurantCode", 0); 또는 "등록된 레스토랑이 없습니다" 메시지 추가 등
            // }
        }
        return "domain/store/store_main";
    }


}
