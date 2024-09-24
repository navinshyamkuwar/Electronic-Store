package com.pirates.electronic.store.dtos;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemDto{

    private int cartItemId;

    private ProductDto product;

    private int quantity;

    private double totalPrice;

}
