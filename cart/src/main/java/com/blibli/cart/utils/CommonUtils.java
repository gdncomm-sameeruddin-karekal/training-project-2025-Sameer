package com.blibli.cart.utils;

import com.blibli.cart.entity.CartEntity;
import com.blibli.cart.entity.CartItem;
import com.blibli.cart.entityDTO.CartItemRequestDTO;
import org.springframework.beans.BeanUtils;

public class CommonUtils {

    // Entity will be converted to DTO for Product
    public static CartItemRequestDTO getProductRequestDTO(CartItem cartItem) {
        CartItemRequestDTO targetDTO = new CartItemRequestDTO();
        BeanUtils.copyProperties(cartItem, targetDTO);
        return targetDTO;
    }
    // DTO will be converted to Entity for Product
    public static CartItem getProductRequestFromDTO(CartItemRequestDTO productDTO) {
        CartItem target = new CartItem();
        BeanUtils.copyProperties(productDTO, target);
        return target;
    }



}
