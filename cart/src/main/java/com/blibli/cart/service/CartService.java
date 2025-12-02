package com.blibli.cart.service;

import com.blibli.cart.entityDTO.CartItemRequestDTO;
import com.blibli.cart.entityDTO.CartResponseDTO;

import java.util.UUID;

public interface CartService {

    // Requirement: add product to shopping cart
    void addToCart(UUID userId, CartItemRequestDTO request);

    // Requirement: view shopping cart
    CartResponseDTO viewCart(UUID userId);

    // Requirement: delete product from shopping cart
    void deleteItem(UUID userId, String productId);


}
