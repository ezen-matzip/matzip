package com.ezen.matzip.domain.restaurant.repository;

import com.ezen.matzip.domain.restaurant.entity.Menu;
import com.ezen.matzip.domain.restaurant.entity.Restaurant;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Integer> {

    @Query("SELECT m.restaurantCode FROM Menu m WHERE m.menuName LIKE %:menuName%")
    List<Restaurant> findRestaurantsByMenuName(@Param("menuName") String menuName);

    List<Menu> findByRestaurantCode(Restaurant restaurantCode);

    @Query("select m.restaurantCode, sum(CASE WHEN m.menuName LIKE %:menuName% THEN 2 ELSE 0 END) as score " +
            "from Menu m where m.menuName Like %:menuName% " +
            "group by m.restaurantCode.restaurantCode")
    List<Object[]> findRestaurantAndScoreByMenuName(@Param("menuName") String menuName);
}
