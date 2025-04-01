package com.ezen.matzip.domain.review.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
public class ReviewImageDTO {

    private int reviewImageCode;
    private int reviewCode;
    private String reviewImagePath;
    private String reviewOriginalName;
    private String reviewSaveName;

    public ReviewImageDTO(int reviewImageCode, int reviewCode, String reviewImagePath, String reviewOriginalName, String reviewSaveName) {
        this.reviewImageCode = reviewImageCode;
        this.reviewCode = reviewCode;
        this.reviewImagePath = reviewImagePath;
        this.reviewOriginalName = reviewOriginalName;
        this.reviewSaveName = reviewSaveName;
    }

    public ReviewImageDTO(int reviewCode, String reviewImagePath, String reviewOriginalName, String reviewSaveName) {
        this.reviewCode = reviewCode;
        this.reviewImagePath = reviewImagePath;
        this.reviewOriginalName = reviewOriginalName;
        this.reviewSaveName = reviewSaveName;
    }

    public ReviewImageDTO(String reviewImagePath, String reviewOriginalName, String reviewSaveName) {
        this.reviewImagePath = reviewImagePath;
        this.reviewOriginalName = reviewOriginalName;
        this.reviewSaveName = reviewSaveName;
    }
}
