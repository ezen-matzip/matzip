package com.ezen.matzip.domain.restaurant.repository;

import com.ezen.matzip.domain.restaurant.entity.Restaurant;
import com.ezen.matzip.domain.restaurant.entity.Keyword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KeywordRepository extends JpaRepository<Keyword, Integer> {

    List<Keyword> findByRestaurantCode(Restaurant restaurantCode);

    @Query("SELECT m.restaurantCode FROM Menu m WHERE m.menuName LIKE %:keyword%")
    List<Restaurant> findRestaurantsByRestaurantKeyword(@Param("keyword") String keyword);

    @Query("select k.restaurantCode, sum(CASE WHEN k.restaurantKeyword LIKE CONCAT('%', :keyword, '%') THEN 1 ELSE 0 END) as score " +
            "from Keyword k where k.restaurantKeyword Like CONCAT('%', :keyword, '%') " +
            "group by k.restaurantCode.restaurantCode")
    List<Object[]> findRestaurantAndScoreByRestaurantKeyword(@Param("keyword") String keyword);

//    @Query(value = "SELECT k.restaurant_code, SUM(CASE " +
//            "WHEN k.restaurant_keyword LIKE ANY (array[:keywords]) THEN 1 ELSE 0 END) AS score " +
//            "FROM restaurant_star_keyword k " +
//            "WHERE k.restaurant_keyword LIKE ANY (array[:keywords]) " +
//            "GROUP BY k.restaurant_code",
//            nativeQuery = true)
//    List<Object[]> findRestaurantAndScoreByRestaurantKeyword(@Param("keywords") String[] keywords);
}
