package com.ezen.matzip.domain.user.controller;

import com.ezen.matzip.domain.user.entity.Business;
import com.ezen.matzip.domain.user.service.BusinessUpdateService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
public class UpdateBusinessController {

    @Autowired
    private BusinessUpdateService businessUpdateService;

    @GetMapping("/business/myinfo")
    public String editBusinessInfo(Model model, Principal principal) {
        Business user = businessUpdateService.findByUserId(principal.getName());
        model.addAttribute("user", user);

        return "domain/store/store_myinfo";
    }

    @PostMapping("/business/myinfo")
    public String updateBusinessInfo(
            @ModelAttribute("user") @Valid Business formBusiness,
            BindingResult result,
            @RequestParam(value = "password", required = false) String password,
            @RequestParam(value = "passwordCheck", required = false) String passwordCheck,
            Model model,
            Principal principal) {

        Business existingBusiness = businessUpdateService.findByUserId(principal.getName());
        if (existingBusiness == null) {
            result.reject("user", "사업자 정보를 찾을 수 업습니다.");
            model.addAttribute("user", Business.builder().build());
            return "domain/store/store_myinfo";
        }

        if (password != null && !password.trim().isEmpty()) {
            if (!password.equals(passwordCheck)) {
                result.rejectValue("password", "error.user", "비밀번호와 비밀번호 확인이 일치하지 않습니다.");
                model.addAttribute("user", existingBusiness);
                return "domain/store/store_myinfo";
            }
            existingBusiness.updatePassword(businessUpdateService.encodePassword(password));
        }

        existingBusiness.updateBusinessInfo(
                formBusiness.getPhoneNumber(),
                formBusiness.getPasswordQuestion(),
                formBusiness.getPasswordAnswer(),
                formBusiness.getEmail(),
                formBusiness.getRestaurantName()
        );

        if (result.hasErrors()) {
            model.addAttribute("user", existingBusiness);
            return "domain/store/store_myinfo";
        }

        businessUpdateService.updateBusiness(existingBusiness);
        return "redirect:/business/myinfo";
    }

}
