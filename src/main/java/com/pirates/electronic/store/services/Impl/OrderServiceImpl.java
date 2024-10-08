package com.pirates.electronic.store.services.Impl;

import com.pirates.electronic.store.dtos.CreateOrderRequest;
import com.pirates.electronic.store.dtos.OrderDto;
import com.pirates.electronic.store.dtos.PageableResponse;
import com.pirates.electronic.store.entities.*;
import com.pirates.electronic.store.exceptions.BadApiRequestException;
import com.pirates.electronic.store.exceptions.ResourceNotFoundException;
import com.pirates.electronic.store.helper.Helper;
import com.pirates.electronic.store.repositories.CartRepository;
import com.pirates.electronic.store.repositories.OrderRepository;
import com.pirates.electronic.store.repositories.UserRepository;
import com.pirates.electronic.store.services.OrderService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ModelMapper mapper;

    @Override
    public OrderDto createOrder(CreateOrderRequest orderDto) {
        String userId = orderDto.getUserId();
        String cartId = orderDto.getCartId();
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found with given id !!"));
        Cart cart = cartRepository.findById(cartId).orElseThrow(() -> new ResourceNotFoundException("Cart with given id not found on server !!"));
        List<CartItem> cartItems = cart.getItems();

        if(cartItems.isEmpty()){
            throw new BadApiRequestException("Invalid number of items in cart");
        }
        Order order = Order.builder()
                .billingName(orderDto.getBillingName())
                .billingPhone(orderDto.getBillingPhone())
                .billingAddress(orderDto.getBillingAddress())
                .orderedDate(new Date())
                .delivereDate(null)
                .paymentStatus(orderDto.getPaymentStatus())
                .orderStatus(orderDto.getOrderStatus())
                .orderId(UUID.randomUUID().toString())
                .user(user)
                .build();

        AtomicReference<Double> orderAmount = new AtomicReference<>(0.0);
        List<OrderItem> orderItems = cartItems.stream().map(cartItem -> {
            double totalPrice = cartItem.getQuantity() * cartItem.getProduct().getDiscountedPrice();
            // Update orderAmount using AtomicReference
            orderAmount.updateAndGet(currentAmount -> currentAmount + totalPrice);
            //CartItem to OrderItem
            OrderItem orderItem = OrderItem.builder()
                    .quantity(cartItem.getQuantity())
                    .product(cartItem.getProduct())
                    .totalPrice(cartItem.getQuantity() * cartItem.getProduct().getDiscountedPrice())
                    .order(order)
                    .build();

            return orderItem;
        }).collect(Collectors.toList());
        order.setOrderItems(orderItems);
        order.setOrderAmount(orderAmount.get());
        cartRepository.save(cart);
        Order savedOrder = orderRepository.save(order);
        cart.getItems().clear();
        return mapper.map(savedOrder, OrderDto.class);
    }

    @Override
    public void removeOrder(String orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order not found with given id."));
        orderRepository.delete(order);
    }

    @Override
    public List<OrderDto> getOrdersOfUser(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("No user found with given id !!"));
        List<Order> orders = orderRepository.findByUser(user);
        List<OrderDto> orderDtos = orders.stream().map(order -> mapper.map(order, OrderDto.class)).collect(Collectors.toList());
        return orderDtos;
    }

    @Override
    public PageableResponse<OrderDto> getAllOrders(int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort = (sortDir.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()):(Sort.by(sortBy).ascending());
        Pageable pageable = PageRequest.of(pageNumber,pageSize,sort);
        Page<Order> page = orderRepository.findAll(pageable);
        return Helper.getPagableResponse(page,OrderDto.class);
    }
}
