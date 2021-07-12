package com.moneyroomba.service.exception.Category;

public class ParentCategoryIsSameCategory extends RuntimeException {

    public ParentCategoryIsSameCategory(String message) {
        super(message);
    }
}
