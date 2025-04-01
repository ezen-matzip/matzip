package com.ezen.matzip.domain.user.repository;

<<<<<<< HEAD
public class UserRepository {
=======
import com.ezen.matzip.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUserId(String userId);
    boolean existsByUserId(String userId);

    // ★ 추가: 이름, 질문, 답변까지 함께 조회
    Optional<User> findByUserIdAndNameAndPasswordQuestionAndPasswordAnswer(
            String userId,
            String name,
            String passwordQuestion,
            String passwordAnswer
    );
>>>>>>> a9292fb8e498916dfeb81cd7c6ea8e23c2f1c68f
}
