package comidev.gatewayservice.jwt;

import java.io.IOException;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import comidev.gatewayservice.utils.Console;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
    private JwtService jwtService;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, JwtService jwtService) {
        super(authenticationManager);
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String requestURI = request.getRequestURI();
        Console.log("***JwtAuthorizationFilter.doFilterInternal D:*** | URI: " + requestURI);
        if (!requestURI.equals("/login")
                && !requestURI.equals("/users/token/refresh")) {
            String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
            if (jwtService.isBearer(authHeader)) {
                String username = jwtService.username(authHeader);
                List<SimpleGrantedAuthority> authorities = jwtService.roles(authHeader).stream()
                        .map(SimpleGrantedAuthority::new)
                        .toList();
                SecurityContextHolder.getContext()
                        .setAuthentication(new UsernamePasswordAuthenticationToken(
                                username, null, authorities));
            }
        }
        chain.doFilter(request, response);
    }
}
