package br.com.zup.proposta.cartao;

import br.com.zup.proposta.cartao.aviso.Aviso;
import br.com.zup.proposta.cartao.bloqueio.Bloqueio;
import br.com.zup.proposta.cartao.carteira.Carteira;
import br.com.zup.proposta.cartao.parcela.Parcela;
import br.com.zup.proposta.cartao.renegociacao.Renegociacao;
import br.com.zup.proposta.cartao.vencimento.Vencimento;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class CartaoResponse {

    private String id;
    private LocalDateTime emitidoEm;
    private String titular;
    private List<Bloqueio> bloqueios;
    private List<Aviso> avisos;
    private List<Carteira> carteiras;
    private List<Parcela> parcelas;
    private BigDecimal limite;
    private Renegociacao renegociacao;
    private Vencimento vencimento;
    private String idProposta;

}
