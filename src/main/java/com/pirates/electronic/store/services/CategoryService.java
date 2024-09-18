package com.pirates.electronic.store.services;

import com.pirates.electronic.store.dtos.CategoryDto;
import com.pirates.electronic.store.dtos.PageableResponse;

import java.util.List;

public interface CategoryService {

    //create
    CategoryDto createCategory(CategoryDto categoryDto);

    //update
    CategoryDto updateCategory(CategoryDto categoryDto, String categoryId);

    //delete
    void deleteCategory(String categoryId);

    //get all
    PageableResponse<CategoryDto> getAllCategories(int pageNumber, int pageSize, String sortBy, String sortDir);

    //get single
    CategoryDto getCategoryById(String categoryId);

    //search
    List<CategoryDto> searchCategory(String keywords);
}
