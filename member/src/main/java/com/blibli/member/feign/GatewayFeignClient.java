package com.blibli.member.feign;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "gateway-service", url = "http://localhost:8080")
public interface GatewayFeignClient {

    // JWT validation, logout handled here later
}
