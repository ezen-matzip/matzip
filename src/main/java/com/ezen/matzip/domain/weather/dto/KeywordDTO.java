package com.ezen.matzip.domain.weather.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class KeywordDTO {

    private int keywordCode;
    private String keyword;
    private String keywordImgPath;
    private String keywordDescription;
}
