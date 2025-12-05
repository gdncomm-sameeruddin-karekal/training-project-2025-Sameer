package com.example.api_gateway.filter;

import com.example.api_gateway.util.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        String path = exchange.getRequest().getURI().getPath();


        // Bypass login/register
        if (path.contains("/member/login") || path.contains("/member/register") || path.contains("/api/product/") || path.contains("/api/member/logout")) {
            return chain.filter(exchange);
        }

        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String token = authHeader.substring(7);

        if (!jwtUtil.isTokenValid(token)) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String rawToken = token.replace("Bearer ", "");
        String key = "blacklisted:" + rawToken;

        if (Boolean.TRUE.equals(redisTemplate.hasKey(key))) {

            log.warn("Rejected: Token is blacklisted (logged out)");
            return onError(exchange, "Invalid or expired token", HttpStatus.UNAUTHORIZED);
        }

        Claims claims = jwtUtil.getClaims(token);

        // Forward user details
        ServerHttpRequest modifiedReq = exchange.getRequest().mutate()
                .header("X-User-Id", claims.getSubject())
                .header("X-User-Email", claims.get("email", String.class))
                .header("X-User-Roles", claims.get("role", String.class))
                .build();

        return chain.filter(exchange.mutate().request(modifiedReq).build());
    }
    @Override
    public int getOrder() {
        return -1;  // High priority
    }

    private Mono<Void> onError(ServerWebExchange exchange, String message, HttpStatus status) {
        exchange.getResponse().setStatusCode(status);
        return exchange.getResponse().setComplete();
    }
}
