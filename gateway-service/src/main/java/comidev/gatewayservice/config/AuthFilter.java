package comidev.gatewayservice.config;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;

import comidev.gatewayservice.dto.JwtDTO;
import comidev.gatewayservice.dto.RequestDTO;
import reactor.core.publisher.Mono;

@Component
public class AuthFilter extends AbstractGatewayFilterFactory<AuthFilter.Config> {

    private WebClient.Builder webClient;

    public AuthFilter(WebClient.Builder webClient) {
        super(Config.class);
        this.webClient = webClient;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            HttpHeaders httpHeaders = request.getHeaders();

            String AUTHORIZATION = HttpHeaders.AUTHORIZATION;
            if (!httpHeaders.containsKey(AUTHORIZATION)) {
                return onError(exchange, HttpStatus.UNAUTHORIZED);
            }

            String bearerToken = httpHeaders.get(AUTHORIZATION).get(0);
            if (!isBearer(bearerToken)) {
                return onError(exchange, HttpStatus.BAD_REQUEST);
            }

            String uri = request.getPath().toString();
            String method = request.getMethod().toString();

            return webClient.build()
                    .post()
                    .uri("http://auth-service/users/token/validate?token=" + bearerToken)
                    .bodyValue(new RequestDTO(uri, method))
                    .retrieve()
                    .bodyToMono(JwtDTO.class)
                    .map(token -> {
                        // exchange.getRequest()
                        // .mutate()
                        // .header("x-auth-user-id", token.getTokenAccess());
                        System.out.println("\n\n TOKEN: " + token.getTokenAccess() + "\n\n");
                        return exchange;
                    })
                    .flatMap(chain::filter);
        });
    }

    private Mono<Void> onError(ServerWebExchange exchange, HttpStatus status) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(status);
        return response.setComplete();
    }

    private boolean isBearer(String authorization) {
        return authorization != null
                && authorization.startsWith("Bearer ")
                && authorization.split("\\.").length == 3;
    }

    protected class Config {
    }
}
