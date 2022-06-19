package comidev.authservice.jwt;

import com.fasterxml.jackson.annotation.JsonAlias;

import lombok.Getter;

@Getter
public class JwtDTO {
    @JsonAlias(value = "access_token")
    private String accessToken;
    @JsonAlias(value = "refresh_token")
    private String refreshToken;

    public JwtDTO(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
