package com.blibli.cart.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartItem {

    private String productId;
    private int quantity;
    private double unitPrice;
    private String productName;


}
