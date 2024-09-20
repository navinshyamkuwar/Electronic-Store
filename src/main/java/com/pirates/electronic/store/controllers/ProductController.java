package com.pirates.electronic.store.controllers;

import com.pirates.electronic.store.dtos.*;
import com.pirates.electronic.store.services.FileService;
import com.pirates.electronic.store.services.ProductService;
import jakarta.servlet.http.HttpServletResponse;
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

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private FileService fileService;

    @Value("${product.image.path}")
    private String imagePath;

    //create
    @PostMapping()
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productDto){
        ProductDto createdProduct = productService.create(productDto);
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }
    //update
    @PutMapping("/{productId}")
    public ResponseEntity<ProductDto> updateProduct(@RequestBody ProductDto productDto, @PathVariable String productId){
        ProductDto updatedProduct = productService.update(productDto,productId);
        return new ResponseEntity<>(updatedProduct,HttpStatus.OK);
    }
    //delete
    @DeleteMapping("/{productId}")
    public ResponseEntity<ApiResponseMessage> deleteProduct(@PathVariable String productId){
        productService.delete(productId);
        ApiResponseMessage responseMessage = ApiResponseMessage.builder().message("Product is deleted successfully.").status(HttpStatus.OK).success(true).build();
        return new ResponseEntity<>(responseMessage,HttpStatus.OK);
    }
    //get single
    @GetMapping("/{productId}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable String productId){
        ProductDto productDto = productService.getProductById(productId);
        return new ResponseEntity<>(productDto, HttpStatus.OK);
    }
    //get all
    @GetMapping
    public ResponseEntity<PageableResponse<ProductDto>> getAllProducts(
            @RequestParam(value = "pageNumber",defaultValue = "0",required = false) int pageNumber,
            @RequestParam(value = "pageSize",defaultValue = "10",required = false) int pageSize,
            @RequestParam(value = "sortBy",defaultValue = "title",required = false) String sortBy,
            @RequestParam(value = "sortDir",defaultValue = "asc",required = false) String sortDir
    ){
        PageableResponse<ProductDto> pageableResponse = productService.getAll(pageNumber,pageSize,sortBy,sortDir);
        return new ResponseEntity<>(pageableResponse,HttpStatus.OK);
    }
    //get live
    @GetMapping("/live")
    public ResponseEntity<PageableResponse<ProductDto>> getAllLiveProducts(
            @RequestParam(value = "pageNumber",defaultValue = "0",required = false) int pageNumber,
            @RequestParam(value = "pageSize",defaultValue = "10",required = false) int pageSize,
            @RequestParam(value = "sortBy",defaultValue = "title",required = false) String sortBy,
            @RequestParam(value = "sortDir",defaultValue = "asc",required = false) String sortDir
    ){
        PageableResponse<ProductDto> pageableResponse = productService.getAllLive(pageNumber,pageSize,sortBy,sortDir);
        return new ResponseEntity<>(pageableResponse,HttpStatus.OK);
    }
    //search
    @GetMapping("/search/{query}")
    public ResponseEntity<PageableResponse<ProductDto>> searchProducts(
            @PathVariable String query,
            @RequestParam(value = "pageNumber",defaultValue = "0",required = false) int pageNumber,
            @RequestParam(value = "pageSize",defaultValue = "10",required = false) int pageSize,
            @RequestParam(value = "sortBy",defaultValue = "title",required = false) String sortBy,
            @RequestParam(value = "sortDir",defaultValue = "asc",required = false) String sortDir
    ){
        PageableResponse<ProductDto> pageableResponse = productService.searchByTitle(query,pageNumber,pageSize,sortBy,sortDir);
        return new ResponseEntity<>(pageableResponse,HttpStatus.OK);
    }
    //upload image
    @PostMapping("/image/{productId}")
    public ResponseEntity<ImageResponse> uploadProductImage(
            @PathVariable String productId,
            @RequestParam("productImage")MultipartFile image
            ) throws IOException {
        String fileName = fileService.uploadFile(image,imagePath);
        ProductDto productDto = productService.getProductById(productId);
        productDto.setProductImage(fileName);
        ProductDto updatedProduct = productService.update(productDto,productId);
        ImageResponse response = ImageResponse.builder().imageName(updatedProduct.getProductImage()).message("Product image is successfully uploaded.").status(HttpStatus.CREATED).success(true).build();
        return new ResponseEntity<>(response,HttpStatus.CREATED);
    }
    //serve image
    @GetMapping("/image/{productId}")
    public void serveProduct(@PathVariable String productId, HttpServletResponse response) throws IOException {

        ProductDto productDto = productService.getProductById(productId);
        InputStream resource = fileService.getResource(imagePath,productDto.getProductImage());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource,response.getOutputStream());

    }
}
