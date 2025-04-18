package com.ezen.matzip.domain.review.repository;

import com.ezen.matzip.domain.restaurant.dto.RestaurantDTO;
import com.ezen.matzip.domain.restaurant.entity.Restaurant;
import com.ezen.matzip.domain.review.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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
    @Query("SELECT r, r.restaurantCode, u.userId, u.nationality " +
            "FROM Review r " +
            "JOIN User u ON r.userCode = u.userCode " +
            "WHERE r.restaurantCode = :restaurantCode AND r.hiddenFlag = 0")
    List<Object[]> findByRestaurantCode(@Param("restaurantCode") Restaurant restaurantCode);



    //완수- 신고수 동기화에 필요
    // 특정 유저(userCode)가 작성한 리뷰들의 신고 수 합계를 반환 (신고가 없으면 0 반환)
    @Query("SELECT COALESCE(SUM(r.reviewReportCount), 0) FROM Review r WHERE r.userCode = :userCode")
    int sumReportCountByUserCode(@Param("userCode") int userCode);

    // 계정 정지 상태 변경 시, 해당 유저의 모든 리뷰의 hiddenFlag를 업데이트 (1: 숨김, 0: 노출)
    @Modifying
    @Query("update Review r set r.hiddenFlag = :hiddenFlag where r.userCode = :userCode")
    void updateHiddenFlagByUserCode(@Param("userCode") int userCode, @Param("hiddenFlag") int hiddenFlag);
    //완수 끝

    int countByReviewReportCountGreaterThan(int reviewReportCount);

    int countByHiddenFlag(int hiddenFlag);

    List<Review> findAllByUserCode(int userCode);

    @Query("SELECT AVG(r.rating) FROM Review r " +
            "JOIN User u ON r.userCode = u.userCode " +
            "WHERE u.nationality = :nationality " +
            "AND r.restaurantCode = :restaurantCode")
    Double getAverageByNationalityAndRestaurant(
            @Param("nationality") String nationality,
            @Param("restaurantCode") Restaurant restaurantCode);


    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.restaurantCode = :restaurantCode")
    Double findAverageRatingByRestaurantCode(@Param("restaurantCode") Restaurant restaurantCode);


//    Double findAverageRatingByRestaurantCode(RestaurantDTO restaurant);
}
