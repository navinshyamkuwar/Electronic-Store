package com.pirates.electronic.store.services.Impl;

import com.pirates.electronic.store.dtos.AddItemToCartRequest;
import com.pirates.electronic.store.dtos.CartDto;
import com.pirates.electronic.store.entities.Cart;
import com.pirates.electronic.store.entities.CartItem;
import com.pirates.electronic.store.entities.Product;
import com.pirates.electronic.store.entities.User;
import com.pirates.electronic.store.exceptions.BadApiRequestException;
import com.pirates.electronic.store.exceptions.ResourceNotFoundException;
import com.pirates.electronic.store.repositories.CartItemRepository;
import com.pirates.electronic.store.repositories.CartRepository;
import com.pirates.electronic.store.repositories.ProductRepository;
import com.pirates.electronic.store.repositories.UserRepository;
import com.pirates.electronic.store.services.CartService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ModelMapper mapper;

    @Override
    public CartDto addItemToCart(String userId, AddItemToCartRequest request) {

        int quantity = request.getQuantity();
        String productId = request.getProductId();

        if(quantity<0){
            throw new BadApiRequestException("Requested quantity is not valid.");
        }

        //fetch the product
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product not found with given id."));
        //fetch the user
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("No user found with given id."));

        Cart cart = null;

        try {
            cart = cartRepository.findByUser(user).get();
        }catch (NoSuchElementException e){
            cart = new Cart();
            cart.setCartId(UUID.randomUUID().toString());
            cart.setCreatedAt(new Date());
        }

        //Perform cart operations
        //if cart item already present then update quantity
        AtomicReference<Boolean> updated = new AtomicReference<>(false);
        List<CartItem> items = cart.getItems();
        List<CartItem > updatedItems = items.stream().map(item ->{
            if(item.getProduct().getProductId().matches(productId)){
                //item already present in cart
                item.setQuantity(quantity);
                item.setTotalPrice(quantity * product.getDiscountedPrice());
                updated.set(true);
            }
            return item;
        }).collect(Collectors.toList());

        cart.setItems(updatedItems);

        if(!updated.get()){
            //create items
            CartItem cartItem = CartItem.builder().
                    quantity(quantity).
                    totalPrice(quantity * product.getDiscountedPrice()).
                    cart(cart).
                    product(product).
                    build();
            cart.getItems().add(cartItem);
        }

        cart.setUser(user);
        Cart updatedCart = cartRepository.save(cart);

        return mapper.map(updatedCart,CartDto.class);
    }

    @Override
    public void removeItemFromCart(String userId, String cartItem) {

        CartItem cartItem1 = cartItemRepository.findById(cartItem).orElseThrow(() -> new ResourceNotFoundException("Cart Item not found !!"));
        cartItemRepository.delete(cartItem1);
    }

    @Override
    public void clearCart(String userId) {

        //fetch the user
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("No user found with given id."));
        //fetch cart
        Cart cart = cartRepository.findByUser(user).orElseThrow(()->new ResourceNotFoundException("Cart of given user not found !!"));
        cart.getItems().clear();
        cartRepository.save(cart);


    }

    @Override
    public CartDto getCartByUser(String userId) {
        //fetch the user
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("No user found with given id."));
        //fetch cart
        Cart cart = cartRepository.findByUser(user).orElseThrow(()->new ResourceNotFoundException("Cart of given user not found !!"));
        return mapper.map(cart, CartDto.class);
    }
}
