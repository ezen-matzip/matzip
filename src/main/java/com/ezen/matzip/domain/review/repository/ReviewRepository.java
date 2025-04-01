package com.ezen.matzip.domain.review.repository;

import com.ezen.matzip.domain.restaurant.entity.Restaurant;
import com.ezen.matzip.domain.review.entity.Review;
import com.ezen.matzip.domain.review.entity.ReviewImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {

    // 특정 사용자(userCode)의 리뷰 목록을 조회하는 메서드
    @Query("SELECT r, r.restaurantCode FROM Review r where r.userCode = :userCode")
    List<Object[]> findByUserCode(int userCode);

    Optional<Review> findByReviewCode(int reviewCode);

    List<Review> findByRestaurantCode(@Param("restaurantCode") Restaurant restaurantCode);

}
