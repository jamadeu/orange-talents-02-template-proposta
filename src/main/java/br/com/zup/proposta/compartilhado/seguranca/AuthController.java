package br.com.zup.proposta.compartilhado.seguranca;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthClient authClient;

    public AuthController(AuthClient authClient) {
        this.authClient = authClient;
    }

    @PostMapping
    public LoginResponse auth(@RequestBody LoginRequest request) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<String, String>();
        formData.add("grant_type", "password");
        formData.add("username", request.getUsername());
        formData.add("password", request.getPassword());
        formData.add("client_id", request.getClientId());
        formData.add("scope", request.getScope());

        return authClient.auth(formData);
    }
}
