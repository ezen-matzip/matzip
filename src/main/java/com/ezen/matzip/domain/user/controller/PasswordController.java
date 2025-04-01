package com.ezen.matzip.domain.user.controller;

import com.ezen.matzip.domain.user.service.PasswordResetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
public class PasswordController {

    private final PasswordResetService passwordResetService;

    @Autowired
    public PasswordController(PasswordResetService passwordResetService) {
        this.passwordResetService = passwordResetService;
    }

    @GetMapping("/findpwd")
    public String findPwd() {
        return "common/findpwd";
    }

    @PostMapping("checkUserInfo")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> checkUserInfo(
            @RequestParam String userId,
            @RequestParam String name,
            @RequestParam String passwordQuestion,
            @RequestParam String passwordAnswer) {

        Map<String, Object> response = passwordResetService.verifyUserInfo(userId, name, passwordQuestion, passwordAnswer);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/resetPassword")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> resetPassword(
            @RequestParam String type,
            @RequestParam String userId,
            @RequestParam String newPassword) {

        Map<String, Object> response = passwordResetService.resetPassword(type, userId, newPassword);
        return ResponseEntity.ok(response);
    }

}
