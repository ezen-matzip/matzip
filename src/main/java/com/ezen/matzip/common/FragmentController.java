package com.ezen.matzip.common;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FragmentController {

    // 유저
//    @GetMapping("/fragments/userheader")
//    public String getUserHeader() {
//        return "html/user_header";
//    }
//
//    @GetMapping("/fragments/userfooter")
//    public String getUserFooter() {
//        return "html/user_footer";
//    }

    // 사업자
    @GetMapping("/fragments/storeheader")
    public String getStoreHeader() {
        return "html/store_header";
    }

    @GetMapping("/fragments/storefooter")
    public String getStoreFooter() {
        return "html/store_footer";
    }

    // 관리자
    @GetMapping("/fragments/adminheader")
    public String getAdminHeader() {
        return "html/admin_header";
    }

    @GetMapping("/fragments/adminfooter")
    public String getAdminFooter() {
        return "html/admin_footer";
    }

}
