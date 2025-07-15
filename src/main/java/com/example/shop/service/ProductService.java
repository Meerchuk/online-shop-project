package com.example.shop.service;

import com.example.shop.dto.productDto.ProductRequestDto;
import com.example.shop.entity.Product;
import com.example.shop.mapper.ProductMapper;
import com.example.shop.repository.CartItemRepository;
import com.example.shop.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CartItemRepository cartItemRepository;

    @Autowired
    public ProductService(ProductRepository productRepository, ProductMapper productMapper, CartItemRepository cartItemRepository) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.cartItemRepository= cartItemRepository;
    }


    @Transactional
    public void createProduct(ProductRequestDto dto) {
        System.out.println("DTO price: " + dto.getPrice());
        if (dto.getPrice() == null) {
            throw new IllegalArgumentException("Price could not be empty");
        }
        if (dto.getQuantity() == null) {
            dto.setQuantity(0);
        }

        Product product = new Product();
        product.setName(dto.getName());
        product.setPrice(dto.getPrice());
        product.setQuantity(dto.getQuantity());
        product.setImageUrl(dto.getImageUrl());

        System.out.println("Saving product with price: " + product.getPrice());

        productRepository.save(product);
    }



    @Transactional
    public void addProduct(Product product) {
        productRepository.save(product);
    }


    public Product findProductById(Integer id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

    }

    public boolean isAvailable(Integer id, Integer requiredQuantity) {
        Optional<Product> optional = productRepository.findById(id);
        return optional.map(p -> p.getQuantity() >= requiredQuantity).orElse(false);

    }


    @Transactional
    public void decraseQuantity(Integer id, Integer amount) {
        Product product = findProductById(id);
        Integer productQuantity = product.getQuantity();
        if (productQuantity < amount) {
            throw new IllegalArgumentException("Not enough stock. Requested: " + amount + "\n" +
                    "Available:" + product.getQuantity());
        }
        product.setQuantity(productQuantity - amount);
        productRepository.save(product);

    }


    @Transactional
    public void incraseQuantity(Integer id, Integer amount) {
        Product product = findProductById(id);
        product.setQuantity(product.getQuantity() + amount);
        productRepository.save(product);
    }


    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }


    @Transactional
    public Product updateProduct(Product updatedProduct) {
        Product existingProduct = productRepository.findById(updatedProduct.getId()).
                orElseThrow(() -> new IllegalArgumentException("Product not found"));
        existingProduct.setName(updatedProduct.getName());
        existingProduct.setPrice(updatedProduct.getPrice());
        existingProduct.setQuantity(updatedProduct.getQuantity());
        return productRepository.save(existingProduct);
    }


    @Transactional
    public void deleteProduct(Integer id) {
        if (!productRepository.existsById(id)) {
            throw new IllegalArgumentException("Product with id " + id + " not found");
        }
        cartItemRepository.deleteByProductId(id);
        productRepository.deleteById(id);
    }

}
