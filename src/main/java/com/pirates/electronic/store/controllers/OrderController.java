package com.pirates.electronic.store.controllers;

import com.pirates.electronic.store.dtos.ApiResponseMessage;
import com.pirates.electronic.store.dtos.CreateOrderRequest;
import com.pirates.electronic.store.dtos.OrderDto;
import com.pirates.electronic.store.dtos.PageableResponse;
import com.pirates.electronic.store.services.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    //create
    @PostMapping
    public ResponseEntity<OrderDto> createOrder(@Valid @RequestBody CreateOrderRequest request){
        OrderDto order = orderService.createOrder(request);
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }
    //remove
    @DeleteMapping("/{orderId}")
    public ResponseEntity<ApiResponseMessage> removeOrder(@PathVariable String orderId){
        orderService.removeOrder(orderId);
        ApiResponseMessage responseMessage = ApiResponseMessage.builder().status(HttpStatus.OK).message("Order is removed !!").success(true).build();
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }
    //get orders of user
    @GetMapping("/users/{userId}")
    public ResponseEntity<List<OrderDto>> getOrdersOfUser(@PathVariable String userId){
        List<OrderDto> ordersOfUser = orderService.getOrdersOfUser(userId);
        return new ResponseEntity<>(ordersOfUser,HttpStatus.OK);
    }
    //get all orders
    @GetMapping()
    public ResponseEntity<PageableResponse<OrderDto>> getAllOrders(
            @RequestParam(value = "pageNumber",defaultValue = "0",required = false) int pageNumber,
            @RequestParam(value = "pageSize",defaultValue = "10",required = false) int pageSize,
            @RequestParam(value = "sortBy",defaultValue = "orderedDate",required = false) String sortBy,
            @RequestParam(value = "sortDir",defaultValue = "desc",required = false) String sortDir
    ){
        PageableResponse<OrderDto> orders = orderService.getAllOrders(pageNumber,pageSize,sortBy,sortDir);
        return new ResponseEntity<>(orders,HttpStatus.OK);
    }


}
