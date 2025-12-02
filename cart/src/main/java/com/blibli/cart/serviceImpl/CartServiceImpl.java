package com.blibli.cart.serviceImpl;

import com.blibli.cart.entity.CartEntity;
import com.blibli.cart.entity.CartItem;
import com.blibli.cart.entityDTO.*;
import com.blibli.cart.exception.CartNotFoundException;
import com.blibli.cart.feign.ProductFeignClient;
import com.blibli.cart.repository.CartRepository;
import com.blibli.cart.service.CartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;

    @Autowired
    private ProductFeignClient productFeignClient;

    public CartServiceImpl(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    @Override
    public void addToCart(UUID userId, CartItemRequestDTO request) {
        log.info("Adding product {} to user cart {}", request.getProductId(), userId);
        CartEntity cart = cartRepository.findById(userId)
                .orElse(new CartEntity(userId, new ArrayList<>()));

        // check if product exists in product service
        ApiResponse<ProductDTO> product = productFeignClient.getProductById(request.getProductId());
        ProductDTO fetchedProduct = product.getData();

        if (product == null) {
            throw new RuntimeException("Product not found for ID " + request.getProductId());
        }

        // check if item already exists
        Optional<CartItem> existing = cart.getItems().stream()
                .filter(i -> i.getProductId().equals(request.getProductId()))
                .findFirst();

        if (existing.isPresent()) {
            CartItem item = existing.get();
            item.setQuantity(item.getQuantity() + request.getQuantity());
        } else {
            CartItem newItem = CartItem.builder()
                    .productId(request.getProductId())
                    .quantity(request.getQuantity())
                    .unitPrice(fetchedProduct.getProductUnitPrice())
                    .productName((fetchedProduct.getProductName()))
                    .build();

            cart.getItems().add(newItem);
        }

        cartRepository.save(cart);
        log.info("Product {} added to cart {}", request.getProductId(), userId);
    }

    @Override
    public CartResponseDTO viewCart(UUID userId) {

        log.info("Fetching cart for user {}", userId);

        CartEntity cart = cartRepository.findById(userId)
                .orElseThrow(() -> new CartNotFoundException("Cart not found"));

        List<CartItemDetailDTO> detailList = new ArrayList<>();
        double total = 0.0;

        for (CartItem item : cart.getItems()) {

            // Requirement: fetch product details from Product Service
            ApiResponse<ProductDTO>  product = productFeignClient.getProductById(item.getProductId());
            ProductDTO productNew = product.getData();

            double itemTotal = productNew.getProductUnitPrice() * item.getQuantity();
            total += itemTotal;

            detailList.add(
                    new CartItemDetailDTO(
                            item.getProductId(),
                            productNew.getProductName(),
                            productNew.getProductUnitPrice(),
                            item.getQuantity(),
                            itemTotal
                    )
            );
        }

        return new CartResponseDTO(userId, detailList, total);

    }

    @Override
    public void deleteItem(UUID userId, String productId) {

        log.info("Deleting product {} from user cart {}", productId, userId);

        CartEntity cart = cartRepository.findById(userId)
                .orElseThrow(() -> new CartNotFoundException("Cart not found"));

        boolean removed = cart.getItems().removeIf(i -> i.getProductId().equals(productId));

        if (!removed) {
            throw new RuntimeException("Product not present in cart");
        }

        cartRepository.save(cart);
        log.info("Product {} removed from cart {}", productId, userId);

    }

}
