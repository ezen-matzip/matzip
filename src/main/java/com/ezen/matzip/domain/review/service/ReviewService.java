package com.ezen.matzip.domain.review.service;

import com.ezen.matzip.domain.reservation.dto.ReservationDTO;
import com.ezen.matzip.domain.reservation.entity.Reservation;
import com.ezen.matzip.domain.reservation.repository.ReservationRepository;
import com.ezen.matzip.domain.restaurant.entity.Restaurant;
import com.ezen.matzip.domain.restaurant.repository.RestaurantRepository;
import com.ezen.matzip.domain.review.dto.ReviewDTO;
import com.ezen.matzip.domain.review.dto.ReviewImageDTO;
import com.ezen.matzip.domain.review.entity.Review;
import com.ezen.matzip.domain.review.repository.RestaurantReviewRepository;
import com.ezen.matzip.domain.review.entity.ReviewImage;
import com.ezen.matzip.domain.review.repository.ReviewImageRepository;
import com.ezen.matzip.domain.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;


@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final RestaurantReviewRepository restaurantReviewRepository;
    private final ReviewImageRepository reviewImageRepository;
    private final ReservationRepository reservationRepository;
    private final RestaurantRepository restaurantRepository;
    private final ModelMapper modelMapper;

    public List<ReviewDTO> findReviewByUserCode(int userCode) {

        List<Object[]> reviewList = reviewRepository.findByUserCode(userCode);
        List<ReviewDTO> result = new ArrayList<>();
        for (Object[] review : reviewList) {
            Review e = (Review) review[0];
            Restaurant restaurant = (Restaurant) review[1];
            ReviewDTO dto = new ReviewDTO();
            dto.setUserCode(e.getUserCode());
            dto.setRestaurantName(restaurant);
            dto.setReviewCode(e.getReviewCode());
            dto.setReviewDate(e.getReviewDate());
            dto.setReviewContent(e.getReviewContent());
            dto.setRating(e.getRating());

            result.add(dto);
        }
            return result;
    }


    @Transactional
    public void deleteReview(int reviewCode) {
        reviewRepository.deleteById(reviewCode);
    }


//    @Transactional
//    public void modifyReview(ReviewDTO reviewDTO) {
//        System.out.println("수정 요청 받은 리뷰 코드: " + reviewDTO.getReviewCode());
//        System.out.println("수정 요청 받은 평점: " + reviewDTO.getRating());
////        Optional<Review> optionalReview = reviewRepository.findByReviewCode(reviewDTO.getReviewCode());
//        List<Object> reviews = findReviewAndReviewImagesByReviewCode(reviewDTO.getReviewCode());
//        Review foundReview = (Review) reviews.get(0);
//        List<ReviewImage> reviewImages = new ArrayList<>();
//        for (int i = 1; i < reviews.size(); i++) {
//            ReviewImage reviewImage = (ReviewImage) reviews.get(i); // 실제 객체
//            ReviewImageDTO reviewImageDTO = modelMapper.map(reviewImage, ReviewImageDTO.class); // DTO로 변환
//            reviewImageRepository.save(reviewImage);
//            reviewImages.add(reviewImage);
//        }
//
//        // 리뷰 수정
//        foundReview.modifyReview(reviewDTO.getReviewContent(), reviewDTO.getRating());
//        reviewRepository.save(foundReview);
//
//        for( ReviewImage reviewImage : reviewImages ) {
//            reviewImageRepository.delete(reviewImage);
//        }
//
//    }


    @Transactional
    public void modifyReview(ReviewDTO reviewDTO, List<MultipartFile> multiFiles) {
        System.out.println("수정 요청 받은 리뷰 코드: " + reviewDTO.getReviewCode());

        // 기존 리뷰 불러오기
        Review review = reviewRepository.findByReviewCode(reviewDTO.getReviewCode())
                .orElseThrow(() -> new IllegalArgumentException("해당 리뷰 없음"));

        // 기존 리뷰 이미지 삭제 (DB + 파일)
        List<ReviewImage> oldImages = reviewImageRepository.findReviewImagesByReviewCode(review.getReviewCode());
        for (ReviewImage img : oldImages) {
            File oldFile = new File("src/main/resources/static" + img.getReviewImagePath());
            if (oldFile.exists()) oldFile.delete();
            reviewImageRepository.delete(img);
        }

        // 리뷰 내용 수정
        review.modifyReview(reviewDTO.getReviewContent(), reviewDTO.getRating());
        reviewRepository.save(review);

        // 이미지 저장 준비
        List<ReviewImageDTO> files = new ArrayList<>();
        String filePath;

        try {
            File fileDir = new File("src/main/resources/static/img/review");
            if (!fileDir.exists()) fileDir.mkdirs();
            filePath = fileDir.getAbsolutePath();

            int count = 0;
            for (MultipartFile file : multiFiles) {
                if (file.isEmpty()) continue;
                if (count >= 3) break;

                String originFileName = file.getOriginalFilename();
                String ext = originFileName.substring(originFileName.lastIndexOf("."));
                String savedFileName = UUID.randomUUID().toString().replace("-", "") + ext;

                // 실제 저장
                file.transferTo(new File(filePath + "/" + savedFileName));

                // 파일 정보 저장
                files.add(new ReviewImageDTO("/img/review/" + savedFileName, originFileName, savedFileName));
                count++;
            }

            // DB 저장
            for (ReviewImageDTO dto : files) {
                ReviewImage newImage = new ReviewImage(review, dto.getReviewImagePath(), dto.getReviewOriginalName(), dto.getReviewSaveName());
                reviewImageRepository.save(newImage);
            }

        } catch (IOException e) {
            System.out.println("파일 저장 실패");
            e.printStackTrace();
        }
    }


    public List<Object> findReviewAndReviewImagesByReviewCode(int reviewCode) {
        List<Object> result = new ArrayList<>();

        // Optional 처리
        Review review = reviewRepository.findByReviewCode(reviewCode)
                .orElseThrow(() -> new IllegalArgumentException("해당 리뷰를 찾을 수 없습니다. 리뷰 코드: " + reviewCode));
        result.add(review);

        List<ReviewImage> reviewImages = reviewImageRepository.findReviewImagesByReviewCode(reviewCode);
        result.addAll(reviewImages);

        return result;
    }

    public List<ReservationDTO> findReservationByUserCode(int userCode) {
        List<Object[]> reservations = reservationRepository.findReservationByUserCode(userCode);

        List<ReservationDTO> result = new ArrayList<>();
        for (Object[] reservation : reservations) {
            Reservation e = (Reservation) reservation[0];
            Restaurant restaurant = (Restaurant) reservation[1];
            ReservationDTO dto = new ReservationDTO();
            dto.setReservationCode(e.getReservationCode());
            dto.setUserCode(e.getUserCode());
            dto.setReservationDate(e.getReservationDate());
            dto.setReservationTime(e.getReservationTime());
            dto.setReservationPeople(e.getReservationPeople());
            dto.setRestaurantCode(restaurant);
            dto.setRestaurantName(restaurant);
            dto.setRecipe(e.getRecipe());

            result.add(dto);
        }
        return result;
    }

    @Transactional
    public void writeReview(ReviewDTO reviewDTO, List<ReviewImageDTO> reviewImageDTO) {
        // 레스토랑 조회
        Restaurant restaurant = restaurantRepository.findById(reviewDTO.getRestaurantCode())
                .orElseThrow(() -> new IllegalArgumentException("해당 레스토랑을 찾을 수 없습니다."));

        // 리뷰 엔티티 생성
        Review review = Review.builder()
                .reviewContent(reviewDTO.getReviewContent())
                .rating(reviewDTO.getRating())
                .userCode(reviewDTO.getUserCode())
                .reservationCode(reviewDTO.getReservationCode())
                .restaurantCode(restaurant)  // 연관관계 설정
                .businessCode(restaurant.getBusinessCode())
                .build();
        review.writeReview();
        reviewRepository.save(review);
//        List<ReviewImage> reviewImages = new ArrayList<>();
        for (ReviewImageDTO dto : reviewImageDTO) {
            ReviewImage reviewImage = new ReviewImage(
                    review, dto.getReviewImagePath(), dto.getReviewOriginalName(), dto.getReviewSaveName());
            reviewImageRepository.save(reviewImage);
        }

    }
}
