package com.ezen.matzip.domain.restaurant.repository;

import com.ezen.matzip.domain.restaurant.entity.Menu;
import com.ezen.matzip.domain.restaurant.entity.Restaurant;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Integer> {

//    Optional<Restaurant> findByRestaurantCode(int restaurantCode);
    Restaurant findByRestaurantCode(Integer restaurantCode);

    @Query("SELECT r FROM Restaurant r WHERE r.restaurantUniqueKeywords LIKE %:keyword% " +
            "OR r.restaurantDescription LIKE %:keyword% " +
            "OR r.restaurantName LIKE %:keyword% " +
            "OR r.restaurantService LIKE %:keyword% " +
            "OR r.restaurantLocation LIKE %:keyword%")
    List<Restaurant> findByRestaurantInfo(@Param("keyword") String keyword);

    @Query("SELECT r, " +
            "(CASE WHEN r.restaurantUniqueKeywords LIKE %:keyword% THEN 1 ELSE 0 END) + " +
            "(CASE WHEN r.restaurantDescription LIKE %:keyword% THEN 1 ELSE 0 END) + " +
            "(CASE WHEN r.restaurantName LIKE %:keyword% THEN 1 ELSE 0 END) + " +
            "(CASE WHEN r.restaurantService LIKE %:keyword% THEN 1 ELSE 0 END) + " +
            "(CASE WHEN r.restaurantLocation LIKE %:keyword% THEN 1 ELSE 0 END) AS score " +
            "FROM Restaurant r " +
            "WHERE r.restaurantUniqueKeywords LIKE CONCAT('%', :keyword, '%') " +
            "OR r.restaurantDescription LIKE CONCAT('%', :keyword, '%') " +
            "OR r.restaurantName LIKE CONCAT('%', :keyword, '%') " +
            "OR r.restaurantService LIKE CONCAT('%', :keyword, '%') " +
            "OR r.restaurantLocation LIKE CONCAT('%', :keyword, '%') " +
            "ORDER BY score DESC")
    List<Object[]> findRestaurantsByKeywordWithScore(@Param("keyword") String keyword);


//    List<Object[]>

}
