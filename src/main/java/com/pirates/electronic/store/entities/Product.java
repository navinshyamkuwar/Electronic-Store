package com.pirates.electronic.store.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
    @Id
    private String productId;
    private String title;
    @Column(length = 10000)
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
