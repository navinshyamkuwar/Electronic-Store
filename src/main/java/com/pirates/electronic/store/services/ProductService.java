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
    public PageableResponse<ProductDto> getAllLive(int pageNumber,int pageSize, String sortBy, String sortDir);

    //search
    public PageableResponse<ProductDto> searchByTitle(String subTitle, int pageNumber,int pageSize, String sortBy, String sortDir);
}
