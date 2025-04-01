package com.ezen.matzip.domain.restaurant.repository;

import com.ezen.matzip.domain.restaurant.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;


public interface RegistRepository extends JpaRepository <Restaurant, Integer> {

    @Query(value = "SET SQL_SAFE_UPDATES = 0;", nativeQuery = true)
    void SetSafeOff();

    @Query(value = "SET SQL_SAFE_UPDATES = 1;", nativeQuery = true)
    void SetSafeOn();

}

