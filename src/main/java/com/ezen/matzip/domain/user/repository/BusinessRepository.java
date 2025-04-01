package com.ezen.matzip.domain.user.repository;

import com.ezen.matzip.domain.user.entity.Business;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BusinessRepository extends JpaRepository<Business, Integer> {
    Optional<Business> findByUserId(String userId);

//    boolean existsByUserId(String userId);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u WHERE u.userId = :userId")
    boolean existsByUserId(@Param("userId") String userId);

    boolean existsByEmail(String email);
    boolean existsByBusinessNumber(String businessNumber);
    boolean existsByRestaurantName(String restaurantName);
    boolean existsByPhoneNumber(String phoneNumber);

    // ★ 추가: 이름, 질문, 답변까지 함께 조회
    Optional<Business> findByUserIdAndRestaurantNameAndPasswordQuestionAndPasswordAnswer(
            String userId,
            String restaurantName,
            String passwordQuestion,
            String passwordAnswer
    );

}
