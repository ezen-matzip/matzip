package com.ezen.matzip.domain.review.service;

import com.ezen.matzip.domain.restaurant.entity.Restaurant;
import com.ezen.matzip.domain.review.dto.ReviewDTO;
import com.ezen.matzip.domain.review.entity.Review;
import com.ezen.matzip.domain.review.repository.RestaurantReviewRepository;
import com.ezen.matzip.domain.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final RestaurantReviewRepository restaurantReviewRepository;
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

            result.add(dto);
        }
            return result;
    }


//    @Transactional
//    public void deleteReview(int reviewCode) {
//        reviewRepository.deleteById(reviewCode);
//    }

    @Transactional
    public void deleteReview(int reviewCode) {
        reviewRepository.deleteById(reviewCode);
    }

    public List<ReviewDTO> findReviewByRestaurantCode(Restaurant restaurantCode) {

        List<Review> reviewList2 = reviewRepository.findByRestaurantCode(restaurantCode);
        List<ReviewDTO> result = new ArrayList<>();
        for (Review review : reviewList2) {
            Review r = review;
            Restaurant restaurant = (Restaurant) review.getRestaurantCode();
            ReviewDTO dto = new ReviewDTO();
            dto.setUserCode(r.getUserCode());
            dto.setRestaurantName(restaurant);
            dto.setReviewCode(r.getReviewCode());
            dto.setReviewDate(r.getReviewDate());
            dto.setReviewContent(r.getReviewContent());

            System.out.println(dto);
            result.add(dto);
        }
        return result;
    }

}
