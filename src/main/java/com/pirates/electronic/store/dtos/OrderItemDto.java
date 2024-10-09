package com.pirates.electronic.store.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class OrderItemDto {

    private int orderItemId;

    private int quantity;

    private double totalPrice;

    private ProductDto product;

}
