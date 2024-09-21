package com.pirates.electronic.store.services;

import com.pirates.electronic.store.dtos.PageableResponse;
import com.pirates.electronic.store.dtos.ProductDto;

public interface ProductService {

    //create
    ProductDto create(ProductDto productDto);

    //update
    ProductDto update(ProductDto productDto, String productId);

    //delete
    void delete(String productId);

    //get single
    ProductDto getProductById(String productId);

    //get all
    PageableResponse<ProductDto> getAll(int pageNumber,int pageSize, String sortBy, String sortDir);

    //get all:Live
    PageableResponse<ProductDto> getAllLive(int pageNumber,int pageSize, String sortBy, String sortDir);

    //search
    PageableResponse<ProductDto> searchByTitle(String subTitle, int pageNumber,int pageSize, String sortBy, String sortDir);

    //create product with category
     ProductDto createWithCategory(ProductDto productDto,String categoryId);

    //update category of product
    ProductDto updateCategory(String productId, String categoryId);

    PageableResponse<ProductDto> getAllProductOfCategory(String categoryId,int pageNumber,int pageSize, String sortBy,String sortDir);
}
