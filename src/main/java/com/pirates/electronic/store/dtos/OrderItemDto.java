package com.pirates.electronic.store.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class OrderItemDto {

    private String orderItemId;

    private int quantity;

    private double totalPrice;

    private ProductDto product;

}
