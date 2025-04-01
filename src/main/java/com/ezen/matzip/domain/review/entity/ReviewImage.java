package com.ezen.matzip.domain.review.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "review_image")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int reviewImageCode;
    @ManyToOne
    @JoinColumn(name = "review_code")
    private Review reviewCode;
    private String reviewImagePath;
    private String reviewOriginalName;
    private String reviewSaveName;

    @Builder
    public ReviewImage(Review reviewCode, String reviewImagePath, String reviewOriginalName, String reviewSaveName) {
        this.reviewCode = reviewCode;
        this.reviewImagePath = reviewImagePath;
        this.reviewOriginalName = reviewOriginalName;
        this.reviewSaveName = reviewSaveName;
    }
}
