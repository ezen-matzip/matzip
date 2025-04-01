package com.ezen.matzip.common;

import com.ezen.matzip.domain.user.entity.User;
import com.ezen.matzip.domain.user.service.CustomUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

//    @GetMapping("/user/main")
//    public String userMain() {
//        return "main/main";
//    }

    @GetMapping("/user/main")
    public String userMain(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        if (userDetails != null) {
            User user = userDetails.getUser();  // 👈 로그인한 유저 객체
            model.addAttribute("userCode", user.getUserCode());  // 👈 main.html에서 사용 가능
        }
        return "main/main";
    }

    @GetMapping(value={"/", "/main"})
    public String guestMain() {
        return "main/main";
    }
//
//    @GetMapping("/user/main")
//    public String userMain() {
//        return "main/main";
//    }
//
    @GetMapping("/login")
    public String login() {
        return "common/login";
    }

    @GetMapping("/fragments/userheader")
    public String getUserHeader() {
        return "html/user_header";
    }

    @GetMapping("/fragments/userfooter")
    public String getUserFooter() {
        return "html/user_footer";
    }

}
