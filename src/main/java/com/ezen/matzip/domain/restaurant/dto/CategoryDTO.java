package com.ezen.matzip.domain.restaurant.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CategoryDTO {
    private int categoryCode;
    private String category;

    public CategoryDTO(int categoryCode, String category) {
        this.categoryCode = categoryCode;
        this.category = category;
    }
}
