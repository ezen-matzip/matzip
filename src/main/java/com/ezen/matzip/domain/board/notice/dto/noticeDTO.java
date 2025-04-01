package com.ezen.matzip.domain.board.notice.dto;

import com.ezen.matzip.domain.board.notice.entity.noticeEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
public class noticeDTO {
    private String id;
    private String title;
    private String content;
    private LocalDateTime createdAt;

    public noticeDTO(noticeEntity notice) {
        this.id = notice.getId();
        this.title = notice.getTitle();
        this.content = notice.getContent();
        this.createdAt = notice.getCreatedAt();
    }
}
