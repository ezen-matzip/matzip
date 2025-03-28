package com.ezen.matzip.domain.review.repository;

import com.ezen.matzip.domain.restaurant.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RestaurantReviewRepository extends JpaRepository<Restaurant, Integer> {


//    List<Object> findByRestaurantCode(int restaurantCode);

//    @Query("SELECT r.restaurantCode, AVG(r.rating) FROM Review r WHERE r.restaurantCode IN :restaurantCodes GROUP BY r.restaurantCode")
//    List<Object[]> findAvgRatingByRestaurantCodes(@Param("restaurantCodes") List<Integer> restaurantCodes);

//    @Query("SELECT r.reviewCode, r.reviewDate, r.restaurantCode ,r.reviewContent FROM Review r WHERE")

}
