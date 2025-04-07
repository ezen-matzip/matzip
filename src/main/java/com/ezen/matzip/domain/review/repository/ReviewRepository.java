package com.ezen.matzip.domain.review.repository;

import com.ezen.matzip.domain.restaurant.entity.Restaurant;
import com.ezen.matzip.domain.review.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {

    // 특정 사용자(userCode)의 리뷰 목록을 조회하는 메서드
    @Query("SELECT r, r.restaurantCode FROM Review r where r.userCode = :userCode")
    List<Object[]> findByUserCode(int userCode);

    // 리뷰 코드로 단일 리뷰 찾기
    Optional<Review> findByReviewCode(int reviewCode);

    // 식당 코드로 모든 리뷰 가져오기
    List<Review> findByRestaurantCode(@Param("restaurantCode") Restaurant restaurantCode);

//    Page<Review> findByUserCode(int userCode, Pageable pageable);

}
