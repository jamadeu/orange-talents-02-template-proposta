package br.com.zup.proposta.compartilhado.seguranca;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "auth", url = "${api.auth}")
public interface AuthClient {

    @PostMapping
    LoginResponse auth(MultiValueMap<String, String> request);
}
