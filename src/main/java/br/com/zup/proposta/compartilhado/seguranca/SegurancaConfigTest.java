package br.com.zup.proposta.compartilhado.seguranca;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Profile({"test"})
@Configuration
public class SegurancaConfigTest extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().anyRequest().permitAll()
                .and().csrf().disable();

    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().anyRequest();
    }
}
