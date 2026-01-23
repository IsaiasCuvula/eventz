package com.bersyte.eventz.features.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class AuthResponseDto {
    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("refresh_token")
    private String refreshToken;
    @JsonProperty("expires_in")
    private Date expiresIn;

    public AuthResponseDto(
            String token,
            String refreshToken,
            Date expiresIn
    ) {
        this.accessToken = token;
        this.expiresIn = expiresIn;
        this.refreshToken = refreshToken;
    }
}
