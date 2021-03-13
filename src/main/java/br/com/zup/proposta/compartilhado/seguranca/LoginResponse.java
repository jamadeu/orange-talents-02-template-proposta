package br.com.zup.proposta.compartilhado.seguranca;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginResponse {

    @JsonProperty("token_type")
    private final String tokenType;

    @JsonProperty("access_token")
    private final String accessToken;

    @JsonProperty("expires_in")
    private final Integer expiresIn;

    public LoginResponse(String tokenType, String accessToken, Integer expiresIn) {
        this.tokenType = tokenType;
        this.accessToken = accessToken;
        this.expiresIn = expiresIn;
    }

    public String getTokenType() {
        return tokenType;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public Integer getExpiresIn() {
        return expiresIn;
    }
}
