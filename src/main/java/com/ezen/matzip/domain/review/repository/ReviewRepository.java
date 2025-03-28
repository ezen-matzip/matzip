package com.ezen.matzip.domain.review.repository;

import com.ezen.matzip.domain.restaurant.entity.Restaurant;
import com.ezen.matzip.domain.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {

    // 특정 사용자(userCode)의 리뷰 목록을 조회하는 메서드
    @Query("SELECT r, r.restaurantCode FROM Review r where r.userCode = :userCode")
    List<Object[]> findByUserCode(int userCode);

    List<Review> findByRestaurantCode(@Param("restaurantCode") Restaurant restaurantCode);

//    @Query("SELECT r.restaurantCode, AVG(r.rating) FROM Review r WHERE r.restaurantCode IN :restaurantCodes GROUP BY r.restaurantCode")
//    List<Object[]> findAvgRatingByRestaurantCodes(@Param("restaurantCodes") List<Integer> restaurantCodes);

//    @Query("SELECT r.reviewCode, r.reviewDate, r.restaurantCode ,r.reviewContent FROM Review r WHERE")

}
