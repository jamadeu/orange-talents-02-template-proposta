package br.com.zup.proposta.compartilhado.seguranca;

public class LoginRequest {

    private final String username;
    private final String password;
    private final String clientId;
    private final String scope;

    public LoginRequest(String username, String password, String clientId, String scope) {
        this.username = username;
        this.password = password;
        this.clientId = clientId;
        this.scope = scope;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getClientId() {
        return clientId;
    }

    public String getScope() {
        return scope;
    }
}
