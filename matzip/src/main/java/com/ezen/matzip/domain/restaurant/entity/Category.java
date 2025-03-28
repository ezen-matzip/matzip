package com.ezen.matzip.domain.restaurant.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="category")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category {
    @Id
    private int categoryCode;
    private String category;

    @OneToMany(mappedBy = "category", cascade = CascadeType.PERSIST)
    private List<Restaurant> restaurants = new ArrayList<>();

    public Category(int categoryCode, String categoryName) {
        this.categoryCode = categoryCode;
        this.category = categoryName;
    }

}
