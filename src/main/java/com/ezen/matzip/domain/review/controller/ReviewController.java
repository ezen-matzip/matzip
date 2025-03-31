package com.ezen.matzip.domain.review.controller;

import com.ezen.matzip.domain.reservation.dto.ReservationDTO;
import com.ezen.matzip.domain.reservation.entity.Reservation;
import com.ezen.matzip.domain.review.dto.ReviewDTO;
import com.ezen.matzip.domain.review.dto.ReviewImageDTO;
import com.ezen.matzip.domain.review.entity.ReviewImage;
import com.ezen.matzip.domain.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Controller
@RequestMapping("/review")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    @Autowired
    private ResourceLoader resourceLoader;


    @GetMapping(value = {"/{userCode}"})
    public String findReviewByUserCode(@PathVariable int userCode, Model model) {

        List<ReviewDTO> resultReview = reviewService.findReviewByUserCode(userCode);
        model.addAttribute("testReview", resultReview);

        return "review/review_list";
    }

    @GetMapping(value="/myReview/{reviewCode}")
    public String findReviewByReviewCode(@PathVariable int reviewCode, Model model) {
        List<Object> reviewAndImgs = reviewService.findReviewAndReviewImagesByReviewCode(reviewCode);
        ReviewDTO review = (ReviewDTO) reviewAndImgs.get(0);
        model.addAttribute("selectedReview", review);
        List<ReviewImageDTO> imgs = new ArrayList<>();
        for (int i = 1; i < reviewAndImgs.size(); i++) {
            imgs.add((ReviewImageDTO) reviewAndImgs.get(1));
        }
        model.addAttribute("selectedReviewImgs", imgs);
        System.out.println("selected: " + review);
        System.out.println("selectedimg: " + imgs);
        return "review/review_list";
    }

    @PostMapping("/delete/{reviewCode}/{userCode}")
    public String deleteReview(@PathVariable int reviewCode, @PathVariable int userCode) {
        reviewService.deleteReview(reviewCode);  // 리뷰 삭제 처리

        // 삭제 후 해당 userCode의 리뷰 목록으로 리디렉션
        return "redirect:/review/" + userCode;  // 삭제 후 해당 사용자의 리뷰 목록으로 리디렉션
    }

    @GetMapping("/modify")
    public void modifyPage(){}

    @PostMapping("/modify")
    public String modifyReview(@ModelAttribute ReviewDTO reviewDTO, Model model) {
        System.out.println("수정 요청 받은 리뷰 코드: " + reviewDTO.getReviewCode());
        System.out.println("수정 요청 받은 평점: " + reviewDTO.getRating());
        System.out.println("수정 요청 받은 유저 코드: " + reviewDTO.getUserCode());
        List<Object> reviewAndImgs = reviewService.findReviewAndReviewImagesByReviewCode(reviewDTO.getReviewCode());
        reviewAndImgs.remove(reviewAndImgs.get(0));
        reviewService.modifyReview(reviewDTO);
        model.addAttribute("reviewAndImgs", reviewAndImgs);
        System.out.println(reviewDTO.getUserCode());
        return "redirect:/review/" + reviewDTO.getUserCode();
    }

    @GetMapping("/write/{userCode}")
    public String findReservation(@PathVariable int userCode, Model model) {
        List<ReservationDTO> resultReservation = reviewService.findReservationByUserCode(userCode);
        model.addAttribute("reservation", resultReservation);

        return "review/review_write";
    }



    @PostMapping("/save")
    public String saveReview(@ModelAttribute ReviewDTO reviewDTO,
                             @RequestParam List<MultipartFile> multiFiles) throws IOException {
        Resource resource = resourceLoader.getResource("classpath:static/img/review");
        String filePath = null;

        if(!resource.exists())
        {
            String root = "src/resources/static/img/review";
            File file = new File(root);
            file.mkdirs(); // 경로가 없다면 위의 root 경로를 생성하는 메소드

            filePath = file.getAbsolutePath();
        }
        else
            filePath = resource.getFile().getAbsolutePath();
        System.out.println("filePath: " + filePath);
        /** 파일에 관한 정보 저장을 위한 처리 */
        List<ReviewImageDTO> files = new ArrayList<>(); // 파일에 관한 정보 저장할 리스트
        List<String> savedFiles = new ArrayList<>();

        try {
            for (MultipartFile file : multiFiles) {
                /** 파일명 변경 처리 */
                String originFileName = file.getOriginalFilename();
                String ext = originFileName.substring(originFileName.lastIndexOf("."));
                String savedFileName = UUID.randomUUID().toString().replace("-", "") + ext;

                /** 파일정보 등록 */
                files.add(new ReviewImageDTO(filePath, originFileName, savedFileName));

                /** 파일 저장 */
                file.transferTo(new File(filePath + "/" + savedFileName));
                savedFiles.add("static/img/review/" + savedFileName);
            }

//            model.addAttribute("message", "파일 업로드 성공!");
//            model.addAttribute("imgs", savedFiles);
        } catch (Exception e) {
            for (ReviewImageDTO file : files)
            {
                new File(filePath + "/" + file.getReviewSaveName()).delete();
            }
//            model.addAttribute("message", "파일 업로드 실패!");
        }

        reviewService.writeReview(reviewDTO, files);
        return "redirect:/review/" + reviewDTO.getUserCode();
    }


}

//review/ → 리뷰 시스템 (리뷰 작성, 수정, 삭제, 조회)

//Controller → 클라이언트 요청을 처리
//Service → 비즈니스 로직을 처리
//Repository → 데이터베이스 CRUD 처리
//DTO → 클라이언트와 데이터를 주고받을 때 사용하는 객체
//Entity → 실제 데이터베이스 테이블과 매칭되는 객체




