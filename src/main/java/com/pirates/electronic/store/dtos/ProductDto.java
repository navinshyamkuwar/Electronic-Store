package com.pirates.electronic.store.dtos;


import lombok.*;

import java.util.Date;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ProductDto {

    private String productId;
    private String title;
    private String description;
    private double price;
    private double discountedPrice;
    private int quantity;
    private Date addedDate;
    private boolean live;
    private boolean stock;
    private int rating;
    private String brandName;
    private String productImage;
}

