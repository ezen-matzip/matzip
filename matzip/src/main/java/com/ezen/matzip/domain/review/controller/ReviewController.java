package com.ezen.matzip.domain.review.controller;

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
@RequestMapping("review")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;


    @GetMapping(value = {"/{userCode}"})
    public String findReviewByUserCode(@PathVariable int userCode, Model model) {


//        // 세션에서 로그인된 유저의 userCode 가져오기
//        Integer loggedInUserCode = (Integer) session.getAttribute("userCode");
//
//        // 로그인된 유저가 없거나, userCode가 맞지 않으면 접근을 막는다.
//        if (loggedInUserCode == null || loggedInUserCode != userCode) {
//            // 해당 유저의 리뷰 목록이 아닌 경우 접근 제한 처리
//            return "redirect:/login";  // 로그인 페이지로 리디렉션
//        }


        List<ReviewDTO> resultReview = reviewService.findReviewByUserCode(userCode);

        model.addAttribute("review", resultReview);

        return "review/review_list";
    }

    @PostMapping("/delete/{reviewCode}/{userCode}")
    public String deleteReview(@PathVariable int reviewCode, @PathVariable int userCode) {
        reviewService.deleteReview(reviewCode);  // 리뷰 삭제 처리
        // 삭제 후 해당 userCode의 리뷰 목록으로 리디렉션
        return "redirect:/review/" + userCode;  // 삭제 후 해당 사용자의 리뷰 목록으로 리디렉션
    }



}

//review/ → 리뷰 시스템 (리뷰 작성, 수정, 삭제, 조회)

//Controller → 클라이언트 요청을 처리
//Service → 비즈니스 로직을 처리
//Repository → 데이터베이스 CRUD 처리
//DTO → 클라이언트와 데이터를 주고받을 때 사용하는 객체
//Entity → 실제 데이터베이스 테이블과 매칭되는 객체




