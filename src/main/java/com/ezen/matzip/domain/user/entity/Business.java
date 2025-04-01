package com.ezen.matzip.domain.user.entity;

import com.ezen.matzip.domain.Role;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "`business`")
@Builder
public class Business {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "business_code")
    private Integer businessCode;           //pk

    @Column(name = "user_id",nullable = false,unique = true)
    private String userId;              //아이디
    @Column(name = "password",nullable = false)
    private String password;            //비밀번호
    @Column(name = "phone_number",nullable = false,unique = true)
    private String phoneNumber;         //전화번호
    @Column(name = "password_question",nullable = false)
    private String passwordQuestion;    //비밀번호찾기질문
    @Column(name = "password_answer",nullable = false)
    private String passwordAnswer;      //비밀번호찾기답변
    @Column(name = "business_number",nullable = false,unique = true)
    private String businessNumber;      //사업자번호
    @Column(name = "restaurant_name",nullable = false)
    private String restaurantName;      //상호명
    @Column(name = "email",nullable = false,unique = true)
    private String email;               //이메일
    @Enumerated(EnumType.STRING)
    @Column(name = "role",nullable = false)
    private Role role;                  //역할

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public void updatePassword(String newPassword) {
        this.password = newPassword;
    }

    public void updateBusinessInfo(String phoneNumber,
                                   String passwordQuestion,
                                   String passwordAnswer,
                                   String email,
                                   String restaurantName) {
        this.phoneNumber = phoneNumber;
        this.passwordQuestion = passwordQuestion;
        this.passwordAnswer = passwordAnswer;
        this.email = email;
        if (restaurantName != null && !restaurantName.trim().isEmpty()) {
            this.restaurantName = restaurantName;
        }
    }
}

//@ManyToOne
//@JoinColumn(name="categoryCode")
//private Category category;
//private String orderableStatus;
