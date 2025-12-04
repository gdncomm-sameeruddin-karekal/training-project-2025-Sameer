package com.example.api_gateway.filter;

import com.example.api_gateway.util.JwtUtil_OLD;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class AuthFilter implements GlobalFilter, Ordered {

    @Autowired
    private JwtUtil_OLD jwtUtil;

    /**
     * Filter to validate JWT in all requests except /member/login & /member/register
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        String path = exchange.getRequest().getURI().getPath();

        // allow login/register without JWT
        if (path.contains("/member/login") || path.contains("/member/register")) {
            return chain.filter(exchange);
        }

        // read token
        String header = exchange.getRequest().getHeaders().getFirst("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            log.error("Missing JWT token");
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String token = header.substring(7);

        if (!jwtUtil.validateToken(token)) {
            log.error("Invalid token");
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        // add username in header → downstream microservices
        String username = jwtUtil.extractUsername(token);
        ServerHttpRequest modified =
                exchange.getRequest()
                        .mutate()
                        .header("X-User", username)
                        .build();

        return chain.filter(exchange.mutate().request(modified).build());
    }

    @Override
    public int getOrder() {
        return -1; // highest precedence
    }

}
