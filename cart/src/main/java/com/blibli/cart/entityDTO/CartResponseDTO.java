package com.blibli.cart.entityDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CartResponseDTO {
    private UUID cartId;
    private List<CartItemDetailDTO> items;
    private Double totalCartValue;
}
