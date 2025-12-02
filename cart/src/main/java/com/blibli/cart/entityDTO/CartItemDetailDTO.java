package com.blibli.cart.entityDTO;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemDetailDTO {
    private String productId;
    private String productName;
    private Double unitPrice;
    private Integer quantity;
    private Double totalPrice;
}
