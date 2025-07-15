package com.example.shop.service;

import com.example.shop.dto.cartDto.CartDto;
import com.example.shop.entity.Cart;
import com.example.shop.entity.CartItem;
import com.example.shop.entity.Product;
import com.example.shop.entity.User;
import com.example.shop.mapper.CartMapper;
import com.example.shop.repository.CartRepository;
import com.example.shop.repository.ProductRepository;
import com.example.shop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CartItemService cartItemService;
    private final ProductService productService;
    private final CartMapper cartMapper;

    @Autowired
    public CartService(CartRepository cartRepository,
                       ProductRepository productRepository,
                       UserRepository userRepository,
                       CartItemService cartItemService,
                       ProductService productService,
                       CartMapper cartMapper) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.cartItemService = cartItemService;
        this.productService = productService;
        this.cartMapper = cartMapper;
    }

    public CartDto getCartDtoForUser(Integer userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found for user: " + userId));
        return cartMapper.toDto(cart);
    }


    public CartDto getUserCart(Integer userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found for user: " + userId));
        return cartMapper.toDto(cart);
    }


    public List<CartItem> getCartItems(Integer userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found for user: " + userId));
        return cart.getItems();
    }


    @Transactional
    public void addProductToCart(Integer userId, Integer productId, Integer quantity) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + productId));

        Cart userCart = cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Cart newCart = new Cart(user);
                    user.setCart(newCart);
                    return cartRepository.save(newCart);
                });

        for (CartItem item : userCart.getItems()) {
            if (item.getProduct().getId().equals(productId)) {
                item.setQuantity(item.getQuantity() + quantity);
                return;
            }
        }

        CartItem newItem = new CartItem(product, quantity);
        newItem.setCart(userCart);
        userCart.getItems().add(newItem);
    }


    @Transactional
    public void removeProductFromCart(Integer userId, Integer productId) {
        Cart userCart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found for user: " + userId));

        boolean removed = userCart.getItems().removeIf(item -> item.getProduct().getId().equals(productId));

        if (!removed) {
            throw new IllegalArgumentException("Product not found in cart");
        }
    }


    @Transactional
    public void clearCart(Integer userId) {
        Cart userCart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found for user: " + userId));
        userCart.getItems().clear();
    }


    public Integer getTotalItemsCount(Integer userId) {
        Cart userCart = cartRepository.findByUserId(userId)
                .orElse(null);
        if (userCart == null || userCart.getItems() == null)
            return 0;
        return userCart.getItems().stream()
                .mapToInt(CartItem::getQuantity)
                .sum();
    }

    public Integer getTotalCartPrice(Integer userId) {
        Cart userCart = cartRepository.findByUserId(userId)
                .orElse(null);
        if (userCart == null || userCart.getItems() == null)
            return 0;

        return userCart.getItems().stream()
                .mapToInt(item -> item.getQuantity() * item.getProduct().getPrice())
                .sum();
    }


    @Transactional
    public void updateProductQuantity(Integer userId, Integer productId, Integer quantity) {
        Cart userCart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found for user: " + userId));
        if (userCart.getItems().isEmpty())
            throw new IllegalArgumentException("Cart is empty");

        for (CartItem item : userCart.getItems()) {
            if (item.getProduct().getId().equals(productId)) {
                if (quantity <= 0) {
                    userCart.getItems().remove(item);
                } else {
                    item.setQuantity(quantity);
                }
                return;
            }
        }
        throw new IllegalArgumentException("Product not found in cart");
    }


    public boolean isProductInCart(Integer userId, Integer productId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElse(null);
        if (cart == null || cart.getItems() == null) return false;
        return cart.getItems().stream()
                .anyMatch(item -> item.getProduct().getId().equals(productId));
    }


    @Transactional
    public void checkout(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found for user: " + userId));

        if (cart.getItems().isEmpty()) {
            throw new IllegalArgumentException("Cart is empty");
        }

        int totalPrice = 0;
        for (CartItem item : cart.getItems()) {
            Integer productId = item.getProduct().getId();

            Product existingProduct = productRepository.findById(productId)
                    .orElseThrow(() -> new IllegalArgumentException("Product not found"));

            int requestedQty = item.getQuantity();
            int availableQty = existingProduct.getQuantity();

            if (availableQty < requestedQty) {
                throw new IllegalArgumentException("Not enough quantity for product: " + existingProduct.getName());
            }

            totalPrice += existingProduct.getPrice() * requestedQty;
            productService.decraseQuantity(productId, requestedQty);
        }

        int userBalance = user.getCashBalance() != null ? user.getCashBalance() : 0;

        if (userBalance < totalPrice) {
            throw new IllegalArgumentException("Not enough funds on balance");
        } else {
            user.setCashBalance(userBalance - totalPrice);
        }

        cart.getItems().clear();
        userRepository.save(user);
    }

}
