package com.blibli.cart.controller;

import com.blibli.cart.entityDTO.CartItemRequestDTO;
import com.blibli.cart.entityDTO.CartResponseDTO;
import com.blibli.cart.entityDTO.GenericResponse;
import com.blibli.cart.service.CartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/cart")
@Slf4j
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    /**
     * Add item into cart
     */
    @PostMapping("/add")
    public GenericResponse<CartItemRequestDTO> addToCart(
            @RequestHeader("X-USER-ID") UUID userId,
            @RequestBody CartItemRequestDTO request) {

        cartService.addToCart(userId, request);
        return GenericResponse.<CartItemRequestDTO>builder()
                .status("SUCCESS")
                .message("Products fetched and addded to cart")
                .data(request)
                .build();

    }

    // Requirement: view shopping cart
    @GetMapping("/viewCart")
    public GenericResponse<CartResponseDTO> viewCart(
            @RequestHeader("X-USER-ID") UUID userId) {

        //return ResponseEntity.ok(cartService.viewCart(userId));
        System.out.println(userId+"--->");
        return GenericResponse.<CartResponseDTO>builder()
                .status("SUCCESS")
                .data(cartService.viewCart(userId))
                .build();
    }
    @DeleteMapping("/delete/{productId}")
    public GenericResponse<Object> deleteItem(
            @RequestHeader("X-USER-ID") UUID userId,
            @PathVariable String productId) {

        cartService.deleteItem(userId, productId);
        return GenericResponse.builder()
                .status("SUCCESS")
                .message("Products fetched")
                .data(productId)
                .build();
    }
}


