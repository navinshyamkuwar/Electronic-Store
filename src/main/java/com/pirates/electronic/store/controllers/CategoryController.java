package com.pirates.electronic.store.controllers;


import com.pirates.electronic.store.dtos.*;
import com.pirates.electronic.store.services.CategoryService;
import com.pirates.electronic.store.services.FileService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private FileService fileService;

    @Value("${category.profile.image.path}")
    private String imageUploadPath;

    //create
    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryDto categoryDto){
        CategoryDto categoryDto1 = categoryService.createCategory(categoryDto);
        return new ResponseEntity<>(categoryDto1, HttpStatus.CREATED);
    }
    //update
    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> updateCategory(@RequestBody CategoryDto categoryDto, @PathVariable String categoryId){
        CategoryDto updatedCategory = categoryService.updateCategory(categoryDto,categoryId);
        return new ResponseEntity<CategoryDto>(updatedCategory,HttpStatus.OK);
    }

    //delete
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<ApiResponseMessage> deleteCategory(@PathVariable String categoryId){
        categoryService.deleteCategory(categoryId);
        ApiResponseMessage responseMessage = ApiResponseMessage.builder().message("Category is deleted successfully").status(HttpStatus.OK).success(true).build();
        return new ResponseEntity<>(responseMessage,HttpStatus.OK);
    }

    //get all
    @GetMapping
    public ResponseEntity<PageableResponse<CategoryDto>> getAllCategories(
            @RequestParam(value = "pageNumber",defaultValue = "0",required = false) int pageNumber,
            @RequestParam(value = "pageSize",defaultValue = "10",required = false) int pageSize,
            @RequestParam(value = "sortBy",defaultValue = "title",required = false) String sortBy,
            @RequestParam(value = "sortDir",defaultValue = "asc",required = false) String sortDir
    ){
        PageableResponse<CategoryDto> pageableResponse = categoryService.getAllCategories(pageNumber,pageSize,sortBy,sortDir);
        return new ResponseEntity<>(pageableResponse,HttpStatus.OK);
    }

    //get single
    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable String categoryId){
        CategoryDto categoryDto = categoryService.getCategoryById(categoryId);
        return new ResponseEntity<>(categoryDto,HttpStatus.OK);
    }

    @GetMapping("/search/{keywords}")
    public ResponseEntity<List<CategoryDto>> searchCategory(@PathVariable String keywords){
        return new ResponseEntity<>(categoryService.searchCategory(keywords),HttpStatus.OK);
    }

    @PostMapping("/coverImage/{categoryId}")
    public ResponseEntity<ImageResponse> uploadCategoryCoverImage(  @RequestParam("coverImage") MultipartFile coverImage, @PathVariable String categoryId) throws IOException {
        String coverImageName = fileService.uploadFile(coverImage,imageUploadPath);

        CategoryDto category = categoryService.getCategoryById(categoryId);
        category.setCoverImage(coverImageName);
        CategoryDto updatedCategory = categoryService.updateCategory(category,categoryId);
        ImageResponse imageResponse = ImageResponse.builder().imageName(coverImageName).success(true).status(HttpStatus.OK).build();
        return new ResponseEntity<>(imageResponse,HttpStatus.CREATED);
    }

    @GetMapping("/coverImage/{categoryId}")
    public void serveCoverImage(@PathVariable String categoryId, HttpServletResponse response) throws IOException {
        CategoryDto category = categoryService.getCategoryById(categoryId);
        InputStream resource = fileService.getResource(imageUploadPath,category.getCoverImage());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource,response.getOutputStream());
    }
}
