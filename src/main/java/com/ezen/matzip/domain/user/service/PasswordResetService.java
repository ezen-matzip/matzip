package com.ezen.matzip.domain.user.service;

import com.ezen.matzip.domain.user.entity.Business;
import com.ezen.matzip.domain.user.entity.User;
import com.ezen.matzip.domain.user.repository.BusinessRepository;
import com.ezen.matzip.domain.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class PasswordResetService {

    private final UserRepository userRepository;
    private final BusinessRepository businessRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public PasswordResetService(UserRepository userRepository, BusinessRepository businessRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.businessRepository = businessRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Map<String, Object> verifyUserInfo(String userId, String name, String passwordQuestion, String passwordAnswer) {
        Map<String, Object> response = new HashMap<>();

        Optional<User> userOpt = userRepository.findByUserIdAndNameAndPasswordQuestionAndPasswordAnswer(userId, name, passwordQuestion, passwordAnswer);
        if (userOpt.isPresent()) {
            response.put("Success", true);
            response.put("type", "USER");
            response.put("userid", userId);
            return response;
        }

        Optional<Business> BusinessOpt = businessRepository.findByUserIdAndRestaurantNameAndPasswordQuestionAndPasswordAnswer(userId, name, passwordQuestion, passwordAnswer);
        if (BusinessOpt.isPresent()) {
            response.put("Success", true);
            response.put("type", "BUSINESS");
            response.put("userid", userId);
            return response;
        }

        response.put("Success", false);
        response.put("message", "정보가 일치하지 않습니다.");
        return response;
    }

    public Map<String, Object> resetPassword(String type, String userId, String newPassword) {
        Map<String, Object> response = new HashMap<>();
        String encodedPassword = passwordEncoder.encode(newPassword);

        if ("USER".equals(type)) {
            Optional<User> userOpt = userRepository.findByUserId(userId);
            if (userOpt.isPresent()) {
                User  user = userOpt.get();
                user.updatePassword(encodedPassword);
                userRepository.save(user);
                response.put("Success", true);
                response.put("message", "비밀번호가 성공적으로 재설정되었습니다.");
            } else {
                response.put("Success", false);
                response.put("message", "해당 유저를 찾을 수 없습니다.");
            }
        } else if ("Business".equals(type)) {
            Optional<Business> businessOpt = businessRepository.findByUserId(userId);
            if (businessOpt.isPresent()) {
                Business business = businessOpt.get();
                business.updatePassword(encodedPassword);
                businessRepository.save(business);
                response.put("Success", true);
                response.put("message", "비밀번호가 성공적으로 재설정되었습니다.");
            } else {
                response.put("Success", false);
                response.put("message", "해당 사업자를 찾을 수 없습니다.");
            }
        }else {
            response.put("Success", false);
            response.put("message", "잘못된 요청입니다.");
        }
        return response;
    }
}
