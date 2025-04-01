package com.ezen.matzip.domain.review.controller;

import com.ezen.matzip.domain.reservation.dto.ReservationDTO;
import com.ezen.matzip.domain.reservation.entity.Reservation;
import com.ezen.matzip.domain.review.dto.ReviewDTO;
import com.ezen.matzip.domain.review.dto.ReviewImageDTO;
import com.ezen.matzip.domain.review.entity.Review;
import com.ezen.matzip.domain.review.entity.ReviewImage;
import com.ezen.matzip.domain.review.repository.ReviewImageRepository;
import com.ezen.matzip.domain.review.repository.ReviewRepository;
import com.ezen.matzip.domain.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
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
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private ReviewImageRepository reviewImageRepository;
    @Autowired
    private ModelMapper modelMapper;


    @GetMapping(value = {"/{userCode}"})
    public String findReviewByUserCode(@PathVariable int userCode, Model model) {

        List<ReviewDTO> resultReview = reviewService.findReviewByUserCode(userCode);
        model.addAttribute("testReview", resultReview);

        return "review/review_list";
    }

    @GetMapping(value = "/myReview/{reviewCode}")
    public String findReviewByReviewCode(@PathVariable int reviewCode, Model model) {
        Review review = reviewRepository.findByReviewCode(reviewCode)
                .orElseThrow(() -> new IllegalArgumentException("해당 리뷰 없음"));

        List<ReviewImage> imgs = reviewImageRepository.findReviewImagesByReviewCode(reviewCode);
        List<ReviewImageDTO> imgDTOs = imgs.stream()
                .map(img -> modelMapper.map(img, ReviewImageDTO.class))
                .toList();

        model.addAttribute("selectedReview", modelMapper.map(review, ReviewDTO.class));
        model.addAttribute("selectedReviewImgs", imgDTOs);

        return "review/review_list";
    }

    @GetMapping("/imageList/{reviewCode}")
    @ResponseBody
    public List<ReviewImageDTO> getReviewImages(@PathVariable int reviewCode) {
        List<ReviewImage> images = reviewImageRepository.findReviewImagesByReviewCode(reviewCode);
        return images.stream()
                .map(img -> modelMapper.map(img, ReviewImageDTO.class))
                .toList();
    }



    @PostMapping("/delete/{reviewCode}/{userCode}")
    public String deleteReview(@PathVariable int reviewCode, @PathVariable int userCode) {
        reviewService.deleteReview(reviewCode);  // 리뷰 삭제 처리

        // 삭제 후 해당 userCode의 리뷰 목록으로 리디렉션
        return "redirect:/review/" + userCode;  // 삭제 후 해당 사용자의 리뷰 목록으로 리디렉션
    }

    @GetMapping("/modify")
    public void modifyPage(){}

//    @PostMapping("/modify")
//    public String modifyReview(@ModelAttribute ReviewDTO reviewDTO, Model model) {
//        System.out.println("수정 요청 받은 리뷰 코드: " + reviewDTO.getReviewCode());
//        System.out.println("수정 요청 받은 평점: " + reviewDTO.getRating());
//        System.out.println("수정 요청 받은 유저 코드: " + reviewDTO.getUserCode());
//        List<Object> reviewAndImgs = reviewService.findReviewAndReviewImagesByReviewCode(reviewDTO.getReviewCode());
//        reviewAndImgs.remove(reviewAndImgs.get(0));
//        reviewService.modifyReview(reviewDTO);
//        model.addAttribute("reviewAndImgs", reviewAndImgs);
//        System.out.println(reviewDTO.getUserCode());
//        return "redirect:/review/" + reviewDTO.getUserCode();
//    }

    @PostMapping("/modify")
    public String modifyReview(@ModelAttribute ReviewDTO reviewDTO,
                               @RequestParam("multiFiles") List<MultipartFile> multiFiles) {
        reviewService.modifyReview(reviewDTO, multiFiles);
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
            String root = "src/main/resources/static/img/review";
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
            int count = 0;
            for (MultipartFile file : multiFiles) {
                if (count >= 3) break;
                /** 파일명 변경 처리 */
                String originFileName = file.getOriginalFilename();
                String ext = originFileName.substring(originFileName.lastIndexOf("."));
                String savedFileName = UUID.randomUUID().toString().replace("-", "") + ext;

                /** 파일정보 등록 */
                files.add(new ReviewImageDTO("/img/review/" + savedFileName, originFileName, savedFileName));

                /** 파일 저장 */
                file.transferTo(new File(filePath + "/" + savedFileName));
                savedFiles.add("static/img/review/" + savedFileName);

                count++;
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

//    @PostMapping("/save")
//    public String saveReview(@ModelAttribute ReviewDTO reviewDTO,
//                             @RequestParam List<MultipartFile> multiFiles) throws IOException {
//        Resource resource = resourceLoader.getResource("classpath:static/img/review");
//        // 이미지 저장 준비
//        List<ReviewImageDTO> files = new ArrayList<>();
//        String filePath;
//
//        try {
//            File fileDir = new File("src/main/resources/static/img/review");
//            if (!fileDir.exists()) fileDir.mkdirs();
//            filePath = fileDir.getAbsolutePath();
//
//            int count = 0;
//            for (MultipartFile file : multiFiles) {
//                if (file.isEmpty()) continue;
//                if (count >= 3) break;
//
//                String originFileName = file.getOriginalFilename();
//                String ext = originFileName.substring(originFileName.lastIndexOf("."));
//                String savedFileName = UUID.randomUUID().toString().replace("-", "") + ext;
//
//                // 실제 저장
//                file.transferTo(new File(filePath + "/" + savedFileName));
//
//                // 파일 정보 저장
//                files.add(new ReviewImageDTO("/img/review/" + savedFileName, originFileName, savedFileName));
//                count++;
//            }
//
//            // DB 저장
//            for (ReviewImageDTO dto : files) {
//                ReviewImage newImage = new ReviewImage(dto.getReviewImageCode(), dto.getReviewImagePath(), dto.getReviewOriginalName(), dto.getReviewSaveName());
//                reviewImageRepository.save(newImage);
//            }
//
//        } catch (IOException e) {
//            System.out.println("파일 저장 실패");
//            e.printStackTrace();
//        }
//
//        reviewService.writeReview(reviewDTO, files);
//        return "redirect:/review/" + reviewDTO.getUserCode();
//    }

    @PostMapping("/image/delete/{reviewImageCode}")
    @ResponseBody
    public ResponseEntity<?> deleteReviewImage(@PathVariable int reviewImageCode) {
        reviewImageRepository.findById(reviewImageCode).ifPresent(image -> {
            File file = new File("src/main/resources/static" + image.getReviewImagePath());
            if (file.exists()) file.delete(); // 실제 파일 삭제
            reviewImageRepository.deleteById(reviewImageCode); // DB 삭제
        });
        return ResponseEntity.ok().build();
    }



}

//review/ → 리뷰 시스템 (리뷰 작성, 수정, 삭제, 조회)

//Controller → 클라이언트 요청을 처리
//Service → 비즈니스 로직을 처리
//Repository → 데이터베이스 CRUD 처리
//DTO → 클라이언트와 데이터를 주고받을 때 사용하는 객체
//Entity → 실제 데이터베이스 테이블과 매칭되는 객체




