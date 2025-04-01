package com.ezen.matzip.domain.user.service;

import com.ezen.matzip.domain.user.entity.Business;
import com.ezen.matzip.domain.user.repository.BusinessRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class BusinessUpdateService {

    @Autowired
    private BusinessRepository businessRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Business findByUserId(String userId) {
        return businessRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("해당 사용자를 찾을 수 없습니다."));
    }

    public String encodePassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    public void updateBusiness(Business business) {
        businessRepository.save(business);
    }
}
