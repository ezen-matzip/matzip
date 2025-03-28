package com.ezen.matzip.domain.user.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class UserDTO {

    private int userCode;
    private int userId;
    private String password;
    private String phoneNumber;
    private String passwordQuestion;
    private String passwordAnswer;
    private String name;
    private String nationality;
    private int is_vegan;
    private int categoryCode;
    private int userReportCount;
    private int accountStatus;

}
