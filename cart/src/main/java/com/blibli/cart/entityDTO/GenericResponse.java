package com.blibli.cart.entityDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GenericResponse<T> {

    private String status;
    private String message;
    private T data;

    public GenericResponse(String message, boolean b) {
    }
}
