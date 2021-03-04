package br.com.zup.proposta.cartao.carteira;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
public class Carteira {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String idCarteira;

    @NotBlank
    @Email
    @Column(nullable = false)
    private String email;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime associadaEm;

    @NotBlank
    @Column(nullable = false)
    private String emissor;

    @Deprecated
    public Carteira() {
    }

    public Carteira(@NotBlank String idCarteira, @NotBlank @Email String email, @NotNull LocalDateTime associadaEm, @NotBlank String emissor) {
        this.idCarteira = idCarteira;
        this.email = email;
        this.associadaEm = associadaEm;
        this.emissor = emissor;
    }
}
