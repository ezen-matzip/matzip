package com.ezen.matzip.domain.board.notice.Controller;

import com.ezen.matzip.domain.board.notice.dto.noticeDTO;
import com.ezen.matzip.domain.board.notice.service.noticeService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping
public class noticeController {

    private final noticeService noticeService;

    public noticeController(noticeService noticeService) {
        this.noticeService = noticeService;
    }

    // 공지사항 목록 조회 (일반 사용자와 관리자 모두 접근)
    @GetMapping("/board/notice/list")
    public String getAllNotices(Model model, Authentication authentication) {
        List<noticeDTO> notices = noticeService.getAllNotices();
        model.addAttribute("notices", notices);

        // 관리자이면 관리자용 목록 페이지를 반환
        if (authentication != null && authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
            return "domain/board/notice/notice_admin_list";
        }
        return "domain/board/notice/notice_list";
    }

    // 공지사항 상세 조회 (URL: /admin/board/notice/{id})
    @GetMapping("/board/notice/{id}")
    public String getNoticeById(@PathVariable String id, Model model) {
        noticeDTO notice = noticeService.getNoticeById(id);
        model.addAttribute("notice", notice);
        return "domain/board/notice/notice_detail";
    }

    // 공지사항 등록 폼 (관리자 전용) - URL: /admin/board/notice/write
    @GetMapping("/admin/board/notice/write")
    public String createNoticeForm() {
        return "domain/board/notice/notice_write_form";
    }

    @PostMapping("/admin/board/notice/create")
    public String createNotice(@RequestParam String title, @RequestParam String content) {
        noticeService.createNotice(title, content);
        return "redirect:/admin/board/notice/notice_write_form";
    }

    @GetMapping("/admin/board/notice/edit/{id}")
    public String updateNoticeForm(@PathVariable String id, Model model) {
        noticeDTO notice = noticeService.getNoticeById(id);
        model.addAttribute("notice", notice);
        return "domain/board/notice/notice_edit";
    }

    @PostMapping("/admin/board/notice/edit/{id}")
    public String updateNotice(@PathVariable String id, @RequestParam String title, @RequestParam String content) {
        noticeService.updateNotice(id, title, content);
        return "redirect:/admin/board/notice/list";
    }

    @PostMapping("/admin/board/notice/delete/{id}")
    public String deleteNotice(@PathVariable String id) {
        noticeService.deleteNotice(id);
        return "redirect:/admin/board/notice/list";
    }

    @GetMapping("/noticeList")
    public String noticeList() {
        return "domain/board/notice/notice_list";
    }
}


