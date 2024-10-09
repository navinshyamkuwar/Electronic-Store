package com.pirates.electronic.store.dtos;

import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class OrderDto {

    private String orderId;

    private String orderStatus = "PENDING";

    private String paymentStatus = "NOTPAID";

    private int orderAmount;

    private String billingAddress;

    private String billingPhone;

    private String billingName;

    private Date orderedDate = new Date();

    private Date delivereDate;

   //private User userDto;

    private List<OrderItemDto> orderItems = new ArrayList<>();
}
