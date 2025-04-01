package com.ezen.matzip.domain.user.entity;

import com.ezen.matzip.domain.Role;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "`user`")
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_code")
    private Integer userCode;

    @Column(name = "user_id",nullable = false,unique = true)
    private String userId;              //아이디
    @Column(name = "password",nullable = false)
    private String password;            //비밀번호
    @Column(name = "phone_number",unique = true)
    private String phoneNumber;         //전화번호
    @Column(name = "password_question",nullable = false)
    private String passwordQuestion;    //비밀번호찾기질문
    @Column(name = "password_answer",nullable = false)
    private String passwordAnswer;      //비밀번호찾기답변
    @Column(name = "name",nullable = false)
    private String name;                //이름
    @Column(name = "nationality",nullable = false)
    private String nationality;         //국가
    @Builder.Default
    @Column(name = "is_vegan", nullable = false)
    private Integer isVegan = 0;               //비건여부
    @Column(name = "category_code",nullable = false)
    private Integer categoryCode;           //선호음식취향
    @Column(name = "user_report_count", nullable = false)
    private Integer userReportCount;        //신고당한횟수
    @Column(name = "account_status")
    private Integer accountStatus;          //정지여부
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

    // 회원정보 수정 시 업데이트할 수 있는 필드들을 한 번에 변경하는 메서드
    public void updateUserInfo(String name,
                               String email,
                               String phoneNumber,
                               String passwordQuestion,
                               String passwordAnswer,
                               Integer categoryCode,
                               Integer isVegan) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.passwordQuestion = passwordQuestion;
        this.passwordAnswer = passwordAnswer;
        this.categoryCode = categoryCode;
        this.isVegan = isVegan;
    }
}

