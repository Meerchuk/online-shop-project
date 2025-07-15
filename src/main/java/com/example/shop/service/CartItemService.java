package com.example.shop.service;

import com.example.shop.dto.cartItemDto.CartItemDto;
import com.example.shop.entity.Cart;
import com.example.shop.entity.CartItem;
import com.example.shop.entity.Product;
import com.example.shop.mapper.CartItemMapper;
import com.example.shop.repository.CartItemRepository;
import com.example.shop.repository.CartRepository;
import com.example.shop.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartItemService {

    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final CartItemMapper cartItemMapper;

    @Autowired
    public CartItemService(CartItemRepository cartItemRepository,
                           CartRepository cartRepository,
                           ProductRepository productRepository,
                           CartItemMapper cartItemMapper) {
        this.cartItemRepository = cartItemRepository;
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.cartItemMapper = cartItemMapper;
    }


    private Cart findCartById(Integer cartId){
        return cartRepository.findById(cartId)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found with id: " + cartId));
    }


    private Product findProductById(Integer productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + productId));
    }


    @Transactional
    public CartItemDto addOrUpdateItem(Integer cartId, Integer productId, int quantity){
        Cart cart = findCartById(cartId);
        Product product = findProductById(productId);

        for(CartItem item: cart.getItems()){
            if(item.getProduct().getId().equals(productId)){
                item.setQuantity(item.getQuantity() + quantity);
                CartItem saved = cartItemRepository.save(item);
                return cartItemMapper.toDto(saved);
            }
        }
        CartItem newItem = new CartItem(product, quantity);
        newItem.setCart(cart);
        cart.getItems().add(newItem);
        CartItem saved = cartItemRepository.save(newItem);
        return cartItemMapper.toDto(saved);
    }


    @Transactional
    public void removeItem(Integer cartId, Integer productId){
        Cart cart = findCartById(cartId);
        boolean removed = cart.getItems().
                removeIf(item -> item.getProduct().getId().equals(productId));
        if(!removed)
            throw new IllegalArgumentException("Product with id " + productId + " not found in cart");
        cartRepository.save(cart);
    }


    @Transactional
    public void updateQuantity(Integer cartId, Integer productId, int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Not valid quantity");
        }

        CartItem cartItem = cartItemRepository.findByCartIdAndProductId(cartId, productId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "CartItem not found with cartId: " + cartId + " and productId: " + productId
                ));

        cartItem.setQuantity(quantity);
        cartItemRepository.save(cartItem);
    }



    public CartItemDto getCartItem(Integer cartId, Integer productId) {
        CartItem cartItem = cartItemRepository.findByCartIdAndProductId(cartId, productId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "CartItem not found for cartId: " + cartId + " and productId: " + productId));
        return cartItemMapper.toDto(cartItem);
    }


    @Transactional(readOnly = true)
    public List<CartItemDto> getCartItemsByCartId(Integer cartId){
        Cart cart = findCartById(cartId);
        return cart.getItems().stream()
                .map(cartItemMapper::toDto)
                .collect(Collectors.toList());
    }


    @Transactional
    public void clearCartItems(Integer cartId){
        Cart cart = findCartById(cartId);
        List<CartItem> itemsList = cart.getItems();
        itemsList.clear();
    }


    public boolean isProductInCart(Integer cartId, Integer productId){
        Cart cart = findCartById(cartId);
        return cart.getItems().stream().
                anyMatch(item -> item.getProduct().getId().equals(productId));
    }


    @Transactional(readOnly = true)
    public int getTotalQuantityInCart(Integer cartId){
        Cart cart = findCartById(cartId);
        return cart.getItems().stream().mapToInt(CartItem::getQuantity).sum();
    }


    @Transactional(readOnly = true)
    public int getTotalPriceInCart(Integer cartId){
        Cart cart = findCartById(cartId);
        return cart.getItems().stream().
                mapToInt(item -> item.getProduct().getPrice() * item.getQuantity()).sum();
    }

}
