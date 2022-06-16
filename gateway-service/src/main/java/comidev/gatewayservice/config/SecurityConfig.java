package comidev.gatewayservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import comidev.gatewayservice.jwt.JwtAuthorizationFilter;
import comidev.gatewayservice.jwt.JwtService;
import comidev.gatewayservice.user.UserService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    @Autowired
    public UserService userService;
    @Autowired
    public JwtService jwtService;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(daoAuthenticationProvider);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests((authorize) -> authorize
                        .antMatchers("/login", "/users/token/refresh")
                        .permitAll()
                        .antMatchers(HttpMethod.POST, "/users")
                        .permitAll()
                        .anyRequest()
                        .authenticated())
                .addFilter(new UsuarioAuthenticationFilter(
                        authenticationManager(), jwtService))
                .addFilterBefore(new JwtAuthorizationFilter(
                        authenticationManager(), jwtService), UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
