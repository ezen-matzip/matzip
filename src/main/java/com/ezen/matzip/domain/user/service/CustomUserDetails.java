package com.ezen.matzip.domain.user.service;

import com.ezen.matzip.domain.Role;
import com.ezen.matzip.domain.user.entity.Business;
import com.ezen.matzip.domain.user.entity.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
public class CustomUserDetails implements UserDetails {

    private final String username;
    private final String password;
    private final Role role;
    // 일반 회원
    private User user;
    // Business 정보 보관 (사업자 로그인 시에만 사용)
    private Business business;

    public CustomUserDetails(User user) {
        this.username = user.getUserId();
        this.password = user.getPassword();
        this.role = user.getRole();
        this.user = user;
    }

    public CustomUserDetails(Business business) {
        this.username = business.getUserId();
        this.password = business.getPassword();
        this.role = business.getRole();
        this.business = business; // Business 객체를 저장
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public String getRestaurantName() {
        return (business != null) ? business.getRestaurantName() : "";
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
