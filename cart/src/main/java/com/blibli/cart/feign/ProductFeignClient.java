package com.blibli.cart.feign;


import com.blibli.cart.entityDTO.ApiResponse;
import com.blibli.cart.entityDTO.ProductDTO;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "product", url = "http://localhost:8082")
public interface ProductFeignClient {

    @GetMapping("/api/product/viewProduct")
    ApiResponse<ProductDTO> getProductById(@RequestParam("ProductID") String productId);
}
